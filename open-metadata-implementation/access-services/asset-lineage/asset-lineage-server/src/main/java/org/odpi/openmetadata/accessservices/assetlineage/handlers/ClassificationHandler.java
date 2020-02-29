/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.Map;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.immutableQualifiedLineageClassifications;

/**
 * The classification handler maps classifications attached with an entity to an lineage entity.
 */
public class ClassificationHandler {

    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Instantiates a new Classification handler.
     *
     * @param invalidParameterHandler the invalid parameter handler
     */
    public ClassificationHandler(InvalidParameterHandler invalidParameterHandler) {
        this.invalidParameterHandler = invalidParameterHandler;
    }


    /**
     * Gets asset context from the entity by classification type.
     *
     * @param entityDetail the entity for retrieving the classifications attached to it
     * @return the asset context by classification
     */
    public Map<String, Set<GraphContext>> buildClassificationEvent(EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        String methodName = "buildClassificationEvent";
        invalidParameterHandler.validateGUID(entityDetail.getGUID(), GUID_PARAMETER, methodName);
        AssetContext assetContext = new AssetContext();
        buildGraphContext(entityDetail, assetContext);
        return assetContext.getNeighbors();
    }

    /**
     * Build graph context
     *
     * @param entityDetail the start entity
     * @param graph        the graph
     * @return the list
     */
    private void buildGraphContext(EntityDetail entityDetail, AssetContext graph) {
        Converter converter = new Converter();
        LineageEntity originalEntityVertex = converter.createLineageEntity(entityDetail);
        graph.addVertex(originalEntityVertex);

        if (entityDetail.getClassifications() == null)
            return;

        for (Classification classification : entityDetail.getClassifications()) {
            if (immutableQualifiedLineageClassifications.contains(classification.getName())) {
                LineageEntity classificationVertex = new LineageEntity();
                classificationVertex.setGuid(entityDetail.getGUID());
                copyClassificationProperties(classificationVertex, classification);
                graph.addVertex(classificationVertex);
                GraphContext graphContext = new GraphContext(classificationVertex.getTypeDefName(), originalEntityVertex.getGuid(), originalEntityVertex, classificationVertex);
                graph.addGraphContext(graphContext);
            }
        }
    }

    private void copyClassificationProperties(LineageEntity lineageEntity, Classification classification) {
        lineageEntity.setVersion(classification.getVersion());
        lineageEntity.setTypeDefName(classification.getType().getTypeDefName());
        lineageEntity.setCreatedBy(classification.getCreatedBy());
        lineageEntity.setUpdatedBy(classification.getUpdatedBy());
        lineageEntity.setCreateTime(classification.getCreateTime());
        lineageEntity.setUpdateTime(classification.getUpdateTime());

        Converter converter = new Converter();
        lineageEntity.setProperties(converter.instancePropertiesToMap(classification.getProperties()));

    }
}
