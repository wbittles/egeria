/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.converters;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ProcessCallElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ProcessCallProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * ProcessCallConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * Relationship object into a bean that includes the ProcessCallProperties.
 */
public class ProcessCallConverter<B> extends AssetManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ProcessCallConverter(OMRSRepositoryHelper repositoryHelper,
                                String               serviceName,
                                String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied relationship, return a new instance of the bean.  It is used for beans that
     * represent a simple relationship between two entities.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewRelationshipBean(Class<B>     beanClass,
                                    Relationship relationship,
                                    String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof ProcessCallElement)
            {
                ProcessCallElement    bean                  = (ProcessCallElement) returnBean;
                ProcessCallProperties processCallProperties = new ProcessCallProperties();

                if (relationship != null)
                {
                    bean.setProcessCallHeader(super.getMetadataElementHeader(beanClass, relationship, null, methodName));

                    EntityProxy entityProxy = relationship.getEntityOneProxy();
                    if (entityProxy != null)
                    {
                        bean.setCaller(super.getMetadataElementHeader(beanClass, entityProxy, entityProxy.getClassifications(), methodName));
                    }

                    entityProxy = relationship.getEntityTwoProxy();
                    if (entityProxy != null)
                    {
                        bean.setCalled(super.getMetadataElementHeader(beanClass, entityProxy, entityProxy.getClassifications(), methodName));
                    }

                    /*
                     * The rest of the properties come from the relationship.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(relationship.getProperties());

                    processCallProperties.setQualifiedName(this.getQualifiedName(instanceProperties));
                    processCallProperties.setDescription(this.getDescription(instanceProperties));
                    processCallProperties.setFormula(this.getFormula(instanceProperties));

                    bean.setProcessCallProperties(processCallProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}