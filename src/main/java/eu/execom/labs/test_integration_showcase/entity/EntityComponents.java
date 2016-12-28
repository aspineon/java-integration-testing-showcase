package eu.execom.labs.test_integration_showcase.entity;

/**
 * Marker interface for autowiring of spring beans.
 */
public interface EntityComponents {

    /*
     * Method <code>substring()</code> takes argument 8 to remove String
     * "package" from full package name
     */
    public static final String ENTITY_PACKAGE = EntityComponents.class.getPackage()
                                                                      .toString()
                                                                      .substring(8);

}
