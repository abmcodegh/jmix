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

package io.jmix.uidata.action.filter;

import io.jmix.core.Messages;
import io.jmix.ui.action.ActionType;
import io.jmix.ui.component.Filter;
import io.jmix.ui.component.FilterComponent;
import io.jmix.ui.meta.StudioAction;
import io.jmix.uidata.entity.FilterConfiguration;
import io.jmix.uidata.filter.UiDataFilterSupport;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@StudioAction(category = "Filter Actions", description = "Saves changes to current filter configuration")
@ActionType(FilterSaveAction.ID)
public class FilterSaveAction extends FilterSaveAsAction {

    public static final String ID = "filter_save";

    protected Map<FilterComponent, Object> valuesMap = new HashMap<>();

    public FilterSaveAction() {
        this(ID);
    }

    public FilterSaveAction(String id) {
        super(id);
    }

    @Autowired
    protected void setMessages(Messages messages) {
        this.caption = messages.getMessage("actions.Filter.Save");
        this.messages = messages;
    }

    @Override
    protected boolean isApplicable() {
        return super.isApplicable()
                && filter.getCurrentConfiguration().isModified();
    }

    @Override
    public void execute() {
        Filter.Configuration configuration = filter.getCurrentConfiguration();
        if (configuration == filter.getEmptyConfiguration()) {
            openInputDialog();
        } else {
            FilterConfiguration configurationModel =
                    ((UiDataFilterSupport) filterSupport).loadFilterConfigurationModel(filter, configuration.getId());
            if (configurationModel != null) {
                saveExistedConfigurationModel(configuration, configurationModel);
            } else {
                openInputDialog();
            }
        }
    }

    protected void saveExistedConfigurationModel(Filter.Configuration configuration,
                                                 @Nullable FilterConfiguration existedConfigurationModel) {
        Map<String, Object> valuesMap = filterSupport.initConfigurationValuesMap(configuration);
        ((UiDataFilterSupport) filterSupport).saveConfigurationModel(configuration, existedConfigurationModel);
        filterSupport.resetConfigurationValuesMap(configuration, valuesMap);
        setCurrentFilterConfiguration(configuration);
    }
}
