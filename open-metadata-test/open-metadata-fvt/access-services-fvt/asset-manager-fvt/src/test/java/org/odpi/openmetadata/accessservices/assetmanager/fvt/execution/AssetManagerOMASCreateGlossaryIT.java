/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.fvt.execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.odpi.openmetadata.accessservices.assetmanager.fvt.glossaries.CreateGlossaryTest;
import org.odpi.openmetadata.fvt.utilities.FVTConstants;
import org.odpi.openmetadata.fvt.utilities.FVTResults;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AssetManagerOMASCreateGlossaryIT is the failsafe wrapper for CreateGlossaryTest.
 */
public class AssetManagerOMASCreateGlossaryIT
{
    @ParameterizedTest
    @ValueSource(strings = {FVTConstants.IN_MEMORY_SERVER, FVTConstants.GRAPH_SERVER})
    public void testInvalidParameters(String serverName)
    {
        FVTResults results = CreateGlossaryTest.performFVT(serverName, FVTConstants.SERVER_PLATFORM_URL_ROOT, FVTConstants.USERID);

        results.printResults();
        assertTrue(results.isSuccessful());
    }
}
