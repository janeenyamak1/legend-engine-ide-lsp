/*
 * Copyright 2024 Goldman Sachs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.finos.legend.engine.ide.lsp.extension.features;

import java.util.Map;
import org.finos.legend.engine.ide.lsp.extension.LegendLSPFeature;
import org.finos.legend.engine.ide.lsp.extension.agGrid.TDSRequest;
import org.finos.legend.engine.ide.lsp.extension.execution.LegendExecutionResult;
import org.finos.legend.engine.ide.lsp.extension.state.CancellationToken;
import org.finos.legend.engine.ide.lsp.extension.state.SectionState;

public interface LegendTDSRequestHandler extends LegendLSPFeature
{
    /**
     * Return the Legend execution result for the given tds request by client.
     *
     * @param section         grammar section state
     * @param entityPath      the function entity path
     * @param request         the ag-grid tds request
     * @param inputParameters input parameters to the function
     * @param requestId request id in the form of cancellation token to allow expensive operations on this request to be cancelled
     * @return Legend execution result
     */
    LegendExecutionResult executeLegendTDSRequest(SectionState section, String entityPath, TDSRequest request, Map<String, Object> inputParameters, CancellationToken requestId);
}
