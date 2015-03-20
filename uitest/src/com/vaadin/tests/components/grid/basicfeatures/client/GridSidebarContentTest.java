/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.tests.components.grid.basicfeatures.client;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.tests.components.grid.basicfeatures.GridBasicClientFeaturesTest;
import com.vaadin.tests.components.grid.basicfeatures.element.CustomGridElement;

public class GridSidebarContentTest extends GridBasicClientFeaturesTest {

    @Test
    public void testSidebarWithHidableColumn() {
        openTestURL();
        CustomGridElement gridElement = getGridElement();

        Assert.assertEquals("Sidebar should not be initially present", 0,
                countBySelector(".v-grid-sidebar"));

        selectMenuPath("Component", "Columns", "Column 0", "Hidable");

        gridElement.findElement(By.className("v-grid-sidebar-button")).click();

        WebElement toggle = gridElement.findElement(By
                .className("column-hiding-toggle"));

        Assert.assertEquals("Column 0 should be togglable", "Header (0,0)",
                toggle.getText());

        selectMenuPath("Component", "Columns", "Column 0", "Hidable");
        Assert.assertEquals("Sidebar should disappear without toggable column",
                0, countBySelector(".v-grid-sidebar"));

    }

    private int countBySelector(String cssSelector) {
        return getGridElement().findElements(By.cssSelector(cssSelector))
                .size();
    }

}
