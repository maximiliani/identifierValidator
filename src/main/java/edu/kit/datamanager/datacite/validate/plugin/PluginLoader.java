/*
 * Copyright 2021 Karlsruhe Institute of Technology.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.kit.datamanager.datacite.validate.plugin;

import edu.kit.datamanager.datacite.validate.ValidatorInterface;
import edu.kit.datamanager.datacite.validate.exceptions.ValidationWarning;
import org.datacite.schema.kernel_4.RelatedIdentifierType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class PluginLoader {

    static Logger LOG = LoggerFactory.getLogger(PluginLoader.class);

    public static Map<RelatedIdentifierType, ValidatorInterface> loadPlugins(File plugDir) throws IOException, ValidationWarning {
        if (plugDir == null || plugDir.getAbsolutePath().isBlank()) throw new ValidationWarning("Empty input!");
        File[] plugJars = plugDir.listFiles(new JARFileFilter());
        if (plugJars == null || plugJars.length < 1) throw new ValidationWarning("No plugins found.");
        ClassLoader cl = new URLClassLoader(PluginLoader.fileArrayToURLArray(plugJars));
        List<Class<ValidatorInterface>> plugClasses = PluginLoader.extractClassesFromJARs(plugJars, cl);

        List<ValidatorInterface> validatorInterfaceList = PluginLoader.createPluggableObjects(plugClasses);
        Map<RelatedIdentifierType, ValidatorInterface> result = new HashMap<RelatedIdentifierType, ValidatorInterface>();
        for (ValidatorInterface i : validatorInterfaceList) {
            result.put(i.supportedType(), i);
        }

        return result;
    }

    private static URL[] fileArrayToURLArray(File[] files) throws MalformedURLException {

        URL[] urls = new URL[files.length];
        for (int i = 0; i < files.length; i++) {
            urls[i] = files[i].toURI().toURL();
        }
        return urls;
    }

    private static List<Class<ValidatorInterface>> extractClassesFromJARs(File[] jars, ClassLoader cl) throws IOException, ValidationWarning {

        List<Class<ValidatorInterface>> classes = new ArrayList<Class<ValidatorInterface>>();
        for (File jar : jars) {
            classes.addAll(PluginLoader.extractClassesFromJAR(jar, cl));
        }
        return classes;
    }

    private static List<Class<ValidatorInterface>> extractClassesFromJAR(File jar, ClassLoader cl) throws IOException, ValidationWarning {

        List<Class<ValidatorInterface>> classes = new ArrayList<Class<ValidatorInterface>>();
        JarInputStream jaris = new JarInputStream(new FileInputStream(jar));
        JarEntry ent = null;
        while ((ent = jaris.getNextJarEntry()) != null) {
            if (ent.getName().toLowerCase().endsWith(".class")) {
                try {
                    Class<?> cls = cl.loadClass(ent.getName().substring(0, ent.getName().length() - 6).replace('/', '.'));
                    if (PluginLoader.isPluggableClass(cls)) {
                        classes.add((Class<ValidatorInterface>) cls);
                    }
                } catch (ClassNotFoundException e) {
                    LOG.info("Can't load Class " + ent.getName());
                    throw new ValidationWarning("Can't load Class " + ent.getName(), e);
                }
            }
        }
        jaris.close();
        return classes;
    }

    private static boolean isPluggableClass(Class<?> cls) {

        for (Class<?> i : cls.getInterfaces()) {
            if (i.equals(ValidatorInterface.class)) {
                return true;
            }
        }
        return false;
    }

    private static List<ValidatorInterface> createPluggableObjects(List<Class<ValidatorInterface>> pluggables) throws ValidationWarning {
        List<ValidatorInterface> plugs = new ArrayList<ValidatorInterface>(pluggables.size());
        for (Class<ValidatorInterface> plug : pluggables) {
            try {
                plugs.add(plug.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                LOG.info("Can't instantiate plugin: " + plug.getName());
                throw new ValidationWarning("Can't instantiate plugin: " + plug.getName(), e);
            } catch (IllegalAccessException e) {
                LOG.info("IllegalAccess for plugin: " + plug.getName());
                throw new ValidationWarning("IllegalAccess for plugin: " + plug.getName(), e);
            }
        }
        return plugs;
    }
}
