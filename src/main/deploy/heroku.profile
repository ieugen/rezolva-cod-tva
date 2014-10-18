# osgi bundles
scan-bundle:mvn:org.osgi/org.osgi.compendium/4.3.1

# pax logging
scan-bundle:mvn:org.ops4j.pax.logging/pax-logging-api/1.8.0
scan-bundle:mvn:org.ops4j.pax.logging/pax-logging-log4j2/1.8.0

# felix config admin
scan-bundle:mvn:org.apache.felix/org.apache.felix.configadmin/1.8.0

scan-bundle:mvn:org.apache.felix/org.apache.felix.shell/1.4.3
scan-bundle:mvn:org.apache.felix/org.apache.felix.shell.tui/1.4.1

# felix http service
scan-bundle:mvn:org.apache.felix/org.apache.felix.http.jetty/2.3.0
scan-bundle:mvn:org.apache.felix/org.apache.felix.http.api/2.3.0

# java servlet api
scan-bundle:mvn:org.apache.geronimo.specs/geronimo-servlet_3.0_spec/1.0

# felix scr
scan-bundle:mvn:org.apache.felix/org.apache.felix.scr/1.8.2
scan-bundle:mvn:org.apache.felix/org.apache.felix.metatype/1.0.8

# felix event admin
scan-bundle:mvn:org.apache.felix/org.apache.felix.webconsole.plugins.event/1.1.0

# felix web-console
scan-bundle:mvn:commons-io/commons-io/2.4
scan-bundle:mvn:commons-fileupload/commons-fileupload/1.3.1
scan-bundle:wrap:mvn:org.json/json/20140107
scan-bundle:mvn:org.apache.felix/org.apache.felix.webconsole.plugins.event/1.1.0
scan-bundle:mvn:org.apache.felix/org.apache.felix.webconsole.plugins.ds/1.0.0
scan-bundle:mvn:org.apache.felix/org.apache.felix.webconsole/4.2.2

# this part is osgi-jax-rs-connector
#scan-features:mvn:com.eclipsesource.jaxrs/features/0.0.1-SNAPSHOT/xml/features!/jax-rs-connector,jax-rs-provider-moxy

# use bundles, because feature is not yet published
scan-bundle:mvn:com.eclipsesource.jaxrs/jersey-all/2.10.1
scan-bundle:mvn:com.eclipsesource.jaxrs/consumer/3.0-SR1
scan-bundle:mvn:com.eclipsesource.jaxrs/publisher/4.1

scan-bundle:mvn:org.eclipse.persistence/org.eclipse.persistence.core/2.5.2
scan-bundle:mvn:org.eclipse.persistence/org.eclipse.persistence.moxy/2.5.2
scan-bundle:mvn:com.eclipsesource.jaxrs/provider-moxy/2.0-SR1

# our sample application
# scan-bundle:mvn:com.eclipsesource.jaxrs/jax-rs-sample/0.0.1-SNAPSHOT