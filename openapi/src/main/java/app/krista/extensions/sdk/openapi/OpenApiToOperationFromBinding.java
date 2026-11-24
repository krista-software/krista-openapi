/*
 * Openapi Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.sdk.openapi;

import java.util.concurrent.CompletableFuture;

public interface OpenApiToOperationFromBinding<T, I, O> extends OpenApiOperationBinding<T, I, OpenApiInputValue, OpenApiOutputValue, O> {

    @Override
    T getBinding();

    @Override
    OpenApiOperation getOperation();

    /**
     * Maps binding data to openapi data
     *
     * @param bindingInput
     * @return
     */
    @Override
    OpenApiInputValue mapInput(I bindingInput);

    /**
     * Maps openapi output to binding data
     *
     * @param openApiOutput
     * @return
     */
    @Override
    O mapOutput(OpenApiOutputValue openApiOutput);

    @Override
    O callSync(I bindingInput);

    @Override
    CompletableFuture<O> callAsync(I bindingInput);

}
