package org.onehippo.forge.properties.upgrade;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.hippoecm.repository.ext.UpdaterModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base updater module
 */
public abstract class PropertiesBaseUpdater implements UpdaterModule  {
    protected final static String TAG_V12A = "v12a-properties";
    protected final static String TAG_V16A = "v16a-properties";
    protected final static String TAG_V18A = "v18a-properties";

    protected final static String NAMESPACE = "properties";

    protected final static String INIT_NODE_CND = "properties";
    protected final static String INIT_NODE_NAMESPACE = "properties-namespace";

    protected static final Logger log = LoggerFactory.getLogger(PropertiesUpdater16a.class);

    /**
     * Removes {@link Node} if it exists
     * @param node {@link Node}
     * @param name of the Node
     * @throws RepositoryException
     */
    protected static void removeNode(Node node, String name) throws RepositoryException {
        log.info("Removing subnode '" + name + "' from " + node.getPath() + " (exists=" + node.hasNode(name) + ")");
        if (node.hasNode(name)) {
            node.getNode(name).remove();
        }
    }
}
