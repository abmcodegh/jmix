/*
 * Copyright 2020 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.ui.component;

import io.jmix.core.common.event.Subscription;
import io.jmix.core.querycondition.PropertyCondition;
import io.jmix.ui.model.DataLoader;

import java.util.EventObject;
import java.util.function.Consumer;

/**
 * PropertyFilter is a UI component used for filtering entities returned by the {@link DataLoader}. The component is
 * related to entity property and can automatically render proper layout for setting a condition value. In general case
 * a PropertyFilter layout contains a label with entity property caption, operation label or selector (=, contains,
 * &#62;, etc.) and a field for editing a property value.
 *
 * @param <V> value type
 */
public interface PropertyFilter<V> extends SingleFilterComponent<V> {

    String NAME = "propertyFilter";

    /**
     * @return a {@link PropertyCondition} related to the current property filter
     */
    @Override
    PropertyCondition getQueryCondition();

    /**
     * @return related entity property name
     */
    String getProperty();

    /**
     * Sets related entity property name.
     *
     * @param property entity property name
     */
    void setProperty(String property);

    /**
     * @return a filtering operation
     */
    Operation getOperation();

    /**
     * Sets a filtering operation.
     *
     * @param operation a filtering operation
     */
    void setOperation(Operation operation);

    /**
     * @return whether an operation selector is visible.
     */
    boolean isOperationEditable();

    /**
     * Sets whether an operation selector is visible.
     *
     * @param operationEditable whether an operation selector is visible
     */
    void setOperationEditable(boolean operationEditable);

    /**
     * Adds a listener that is invoked when the {@code operation} property changes.
     *
     * @param listener a listener to add
     * @return a registration object for removing an event listener
     */
    Subscription addOperationChangeListener(Consumer<OperationChangeEvent> listener);

    /**
     * Event sent when the {@code operation} property is changed.
     */
    class OperationChangeEvent extends EventObject {

        protected final Operation newOperation;
        protected final Operation prevOperation;

        public OperationChangeEvent(PropertyFilter source, Operation newOperation, Operation prevOperation) {
            super(source);
            this.prevOperation = prevOperation;
            this.newOperation = newOperation;
        }

        @Override
        public PropertyFilter getSource() {
            return (PropertyFilter) super.getSource();
        }

        /**
         * @return new operation value
         */
        public Operation getNewOperation() {
            return newOperation;
        }

        /**
         * @return previous operation value
         */
        public Operation getPreviousOperation() {
            return prevOperation;
        }
    }

    /**
     * Operation representing corresponding filtering condition.
     */
    enum Operation {
        EQUAL(Type.VALUE),
        NOT_EQUAL(Type.VALUE),
        GREATER(Type.VALUE),
        GREATER_OR_EQUAL(Type.VALUE),
        LESS(Type.VALUE),
        LESS_OR_EQUAL(Type.VALUE),
        CONTAINS(Type.VALUE),
        NOT_CONTAINS(Type.VALUE),
        STARTS_WITH(Type.VALUE),
        ENDS_WITH(Type.VALUE),
        IS_SET(Type.UNARY),
        IS_NOT_SET(Type.UNARY),
//        IN_LIST(Type.LIST),
//        NOT_IN_LIST(Type.LIST),
//        DATE_INTERVAL(Type.INTERVAL),
        ;

        private final Type type;

        Operation(Type type) {
            this.type = type;
        }

        public Type getType() {
            return type;
        }

        /**
         * Operation type representing the required field type for editing
         * a property value with the given operation.
         */
        public enum Type {

            /**
             * Requires a field suitable for editing a property value, e.g.
             * {@link TextField} for String, {@link ComboBox} for enum.
             */
            VALUE,

            /**
             * Requires a field suitable for choosing unary value, e.g. true/false, YES/NO.
             */
            UNARY,

            /**
             * Requires a field suitable for selecting multiple values of
             * the same type as the property value.
             */
            LIST,

            /**
             * Requires a field suitable for selecting a range of values of
             * the same type as the property value.
             */
            INTERVAL
        }
    }
}
