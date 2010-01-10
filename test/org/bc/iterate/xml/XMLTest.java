/*
 * Copyright (c) 2007-2010 Brian Cavalier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bc.iterate.xml;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@SuppressWarnings({"ImplicitNumericConversion"})
public class XMLTest
{
    public final static String XML_DATA = "<?xml version=\"1.0\"?>"
                                          + "<parent>"
                                          + "<children>"
                                          + "<child>Huey</child>"
                                          + "<child>Duey</child>"
                                          + "<child>Louie</child>" 
                                          + "</children>"
                                          + "</parent>";
    private Document document;

    @Before
    public void setUp() throws IOException, JDOMException
    {
        document = XML.parse(new ByteArrayInputStream(XML_DATA.getBytes()));
    }
    @Test
    public void testParse() throws IOException, JDOMException
    {
        Assert.assertNotNull(XML.parse(new ByteArrayInputStream(XML_DATA.getBytes())));
    }

    @Test
    public void testChildrenFunction() throws IOException, JDOMException
    {
        Assert.assertEquals(1, XML.children().apply(document.getRootElement()).size());
        Assert.assertEquals(3, XML.children().apply(document.getRootElement().getChild("children")).size());
    }

    @Test
    public void testChildrenByNameBinaryFunction()
    {
        Assert.assertEquals(1, XML.childrenByName().apply(document.getRootElement(), "children").size());
        Assert.assertEquals(3, XML.childrenByName().apply(document.getRootElement().getChild("children"), "child").size());
    }

    @Test
    public void testChildren()
    {
        Assert.assertEquals(1, XML.children(document.getRootElement()).size());
        Assert.assertEquals(3, XML.children(document.getRootElement().getChild("children")).size());        
    }

    @Test
    public void testNamedChildren()
    {
        Assert.assertEquals(1, XML.children(document.getRootElement(), "children").size());
        Assert.assertEquals(3, XML.children(document.getRootElement().getChild("children"), "child").size());        
    }
}
