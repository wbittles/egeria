/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { createContext, useContext, useState }   from "react";

import PropTypes                                        from "prop-types";

import { RequestContext }                               from "./RequestContext";

import { InteractionContext }                           from "./InteractionContext";



export const TypesContext = createContext();

export const TypesContextConsumer = TypesContext.Consumer;



const TypesContextProvider = (props) => {

  const requestContext         = useContext(RequestContext);
  const interactionContext     = useContext(InteractionContext);



  /*
   * tex object is initially empty
   */
  const [tex, setTex]              = useState({});    



  /*
   * loadTypeInfo function is an asynchronous function that triggers loading of types and (in _loadTypeInfo) sets the state for tex,
   * which can then be accessed by getter functions below
   */
  const loadTypeInfo = (serverName,platformName ) => {

    requestContext.callPOST("server", serverName,  "types", 
    { serverName : serverName, platformName : platformName, enterpriseOption : requestContext.enterpriseOption }, _loadTypeInfo);
  };

  const _loadTypeInfo = (json) => {
    
    if (json !== null) {
      if (json.relatedHTTPCode === 200 ) {
        let typeExplorer = json.typeExplorer;
        if (typeExplorer !== null) {
          setTex(typeExplorer);
          return;
        }
      }
    }   
    /*
     * On failure ...     
     */
    interactionContext.reportFailedOperation("get types for server",json);
  }



  /*
   * Helper function to retrieve entities from tex
   */
  const getEntityTypes = () => {
    if (tex !== undefined && tex.entities !== undefined) {
      return tex.entities;
    }
    else {      
      return null;
    }
  }

  /*
   * Helper function to retrieve relationships from tex
   */
  const getRelationshipTypes = () => {
    if (tex !== undefined && tex.relationships !== undefined) {
      return tex.relationships;
    }
    else {
      return null;
    }
  }

  /*
   * Helper function to retrieve classifications from tex
   */
  const getClassificationTypes = () => {
    if (tex !== undefined && tex.classifications !== undefined) {
      return tex.classifications;
    }
    else {
      return null;
    }
  }

  /*
   * Helper functions to retrieve specific named types from tex
   */
  const getEntityType = (typeName) => {
    if (tex !== undefined && tex.entities !== undefined) {
      return tex.entities[typeName];
    }
    else {
      return null;
    }
  }

  const getRelationshipType = (typeName) => {
    if (tex !== undefined && tex.relationships !== undefined) {
      return tex.relationships[typeName];
    }
    else {
      return null;
    }
  }

  const getClassificationType = (typeName) => {
    if (tex !== undefined && tex.classifications !== undefined) {
      return tex.classifications[typeName];
    }
    else {
      return null;
    }
  }

  const getEnumType = (typeName) => {
    if (tex !== undefined && tex.enums !== undefined) {
      return tex.enums[typeName];
    }
    else {
      return null;
    }
  }

  return (
    <TypesContext.Provider
      value={{
        tex,
        setTex,
        loadTypeInfo,
        _loadTypeInfo,
        getEntityTypes,
        getRelationshipTypes,
        getClassificationTypes,
        getEntityType,
        getRelationshipType,
        getClassificationType,
        getEnumType
      }}
    >
     {props.children}
    </TypesContext.Provider>
  );
};

TypesContextProvider.propTypes = {
  children: PropTypes.node
};

export default TypesContextProvider;

