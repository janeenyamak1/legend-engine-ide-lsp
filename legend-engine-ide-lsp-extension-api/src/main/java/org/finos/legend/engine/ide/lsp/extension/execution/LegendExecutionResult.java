// Copyright 2023 Goldman Sachs
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.finos.legend.engine.ide.lsp.extension.execution;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.finos.legend.engine.ide.lsp.extension.text.TextLocation;

/**
 * The result of executing a {@link LegendCommand}.
 */
public class LegendExecutionResult
{
    private final List<String> ids;
    private final Type type;
    private final String message;
    private final String messageType;
    private final String logMessage;
    private final TextLocation location;

    protected LegendExecutionResult(List<String> ids, Type type, String message, String logMessage, TextLocation location, String messageType)
    {
        Objects.requireNonNull(ids, "ids may not be null").forEach(id -> Objects.requireNonNull(id, "id may not be null"));
        if (ids.isEmpty())
        {
            throw new IllegalArgumentException("ids may not be empty");
        }
        this.ids = ids;
        this.type = Objects.requireNonNull(type, "type is required");
        this.message = Objects.requireNonNull(message, "message is required");
        this.messageType = messageType;
        this.logMessage = logMessage;
        this.location = location;
    }

    /**
     * The location associated with this execution.  When presenting the results to the users,
     * this can be used to navigate to this location to show the code that help with context
     * of such result.  One example: test results linking to the test case
     * @return the location associated with this result; could be null
     */
    public TextLocation getLocation()
    {
        return this.location;
    }

    public String getMessageType()
    {
        return messageType;
    }

    public static LegendExecutionResult errorResult(Throwable t, String message, String entityPath, TextLocation location)
    {
        StringWriter writer = new StringWriter();
        try (PrintWriter pw = new PrintWriter(writer))
        {
            t.printStackTrace(pw);
        }
        String resultMessage;
        if (message != null)
        {
            resultMessage = message;
        }
        else
        {
            String tMessage = t.getMessage();
            resultMessage = (tMessage == null) ? "Error" : tMessage;
        }
        return LegendExecutionResult.newResult(entityPath, Type.ERROR, resultMessage, writer.toString(), location);
    }

    /**
     * Get the result ids. These should be understood hierarchically. For example, the list could consist of an entity
     * path, a test suite id, and a test id.
     *
     * @return result ids
     */
    public List<String> getIds()
    {
        return this.ids;
    }

    /**
     * Return the type of the result: success, failure, warning, or error.
     *
     * @return result type
     */
    public Type getType()
    {
        return this.type;
    }

    /**
     * Return the result message.
     *
     * @return result message
     */
    public String getMessage()
    {
        return this.message;
    }

    /**
     * Return an optional log message.
     *
     * @return log message or null
     */
    public String getLogMessage()
    {
        return this.logMessage;
    }

    /**
     * Return the log message if present. If not present, then the result message will be returned if
     * {@code returnMessageIfAbsent} is true and null if it is false.
     *
     * @param returnMessageIfAbsent whether to return the result message if the log message is absent
     * @return log message, result message, or null
     */
    public String getLogMessage(boolean returnMessageIfAbsent)
    {
        return ((this.logMessage == null) && returnMessageIfAbsent) ? this.message : this.logMessage;
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }

        if (!(other instanceof LegendExecutionResult))
        {
            return false;
        }

        LegendExecutionResult that = (LegendExecutionResult) other;
        return (this.type == that.type) &&
                this.message.equals(that.message) &&
                Objects.equals(this.logMessage, that.logMessage) &&
                this.ids.equals(that.ids)
                && Objects.equals(this.location, that.location);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.ids, this.type, this.message, this.logMessage, this.location);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append('{');
        if (!this.ids.isEmpty())
        {
            int start = builder.length();
            this.ids.forEach(id -> ((builder.length() == start) ? builder.append("ids=[\"") : builder.append("\", \"")).append(id));
            builder.append("] ");
        }
        return builder.append("type=").append(this.type).append(" location=").append(this.location).append('}').toString();
    }

    /**
     * Construct a new Legend execution result.
     *
     * @param ids        result ids
     * @param type       result type
     * @param message    result message
     * @param logMessage log message (optional)
     * @param location location (optional)
     * @return execution result
     */
    public static LegendExecutionResult newResult(List<String> ids, Type type, String message, String logMessage, TextLocation location)
    {
        return new LegendExecutionResult(ids, type, message, logMessage, location, null);
    }

    /**
     * Construct a new Legend execution result.
     *
     * @param id         result id
     * @param type       result type
     * @param message    result message
     * @param logMessage log message (optional)
     * @param location location (optional)
     * @return execution result
     */
    public static LegendExecutionResult newResult(String id, Type type, String message, String logMessage, TextLocation location)
    {
        return newResult(Collections.singletonList(id), type, message, logMessage, location);
    }

    /**
     * Construct a new Legend execution result.
     *
     * @param ids     result ids
     * @param type    result type
     * @param message result message
     * @param location location (optional)
     * @return execution result
     */
    public static LegendExecutionResult newResult(List<String> ids, Type type, String message, TextLocation location)
    {
        return newResult(ids, type, message, null, location);
    }

    /**
     * Construct a new Legend execution result.
     *
     * @param id      result id
     * @param type    result type
     * @param message result message
     * @param location location (optional)
     * @return execution result
     */
    public static LegendExecutionResult newResult(String id, Type type, String message, TextLocation location)
    {
        return newResult(id, type, message, null, location);
    }

    public enum Type
    {
        SUCCESS, FAILURE, WARNING, ERROR
    }
}
