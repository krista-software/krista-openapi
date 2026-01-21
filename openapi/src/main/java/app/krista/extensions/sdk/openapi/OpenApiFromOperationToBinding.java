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

public interface OpenApiFromOperationToBinding<T, I, O> extends OpenApiOperationBinding<T, OpenApiInputValue, I, O, OpenApiOutputValue> {

    @Override
    T getBinding();

    @Override
    OpenApiOperation getOperation();

    @Override
    I mapInput(OpenApiInputValue input);

    @Override
    OpenApiOutputValue mapOutput(O output);

    @Override
    OpenApiOutputValue callSync(OpenApiInputValue bindingInput);

    @Override
    CompletableFuture<OpenApiOutputValue> callAsync(OpenApiInputValue bindingInput);

}
