/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.ieugen.heroku.web.resource;

import org.ops4j.pax.web.extender.whiteboard.ResourceMapping;
import org.ops4j.pax.web.extender.whiteboard.WelcomeFileMapping;
import org.ops4j.pax.web.extender.whiteboard.runtime.DefaultResourceMapping;
import org.ops4j.pax.web.extender.whiteboard.runtime.DefaultWelcomeFileMapping;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

    private ServiceRegistration<ResourceMapping> resource;
    private ServiceRegistration<WelcomeFileMapping> welcomeFile;

    public void start(BundleContext context) {
        DefaultResourceMapping resourceMapping = new DefaultResourceMapping();
        resourceMapping.setAlias("/");
        resourceMapping.setPath("");
        resource = context.registerService(ResourceMapping.class, resourceMapping, null);

        DefaultWelcomeFileMapping welcomeFileMapping = new DefaultWelcomeFileMapping();
        welcomeFileMapping.setRedirect(true);
        welcomeFileMapping.setWelcomeFiles(new String[]{"index.html"});
        welcomeFile = context.registerService(WelcomeFileMapping.class, welcomeFileMapping, null);

    }

    public void stop(BundleContext context) {
        resource.unregister();
        welcomeFile.unregister();
    }

}