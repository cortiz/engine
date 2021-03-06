/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.engine.search;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.craftercms.core.service.Content;
import org.craftercms.engine.service.context.SiteContext;
import org.craftercms.search.exception.SearchException;
import org.craftercms.search.service.Query;
import org.craftercms.search.service.SearchService;
import org.springframework.beans.factory.annotation.Required;

/**
 * {@link SearchService} wrapper that forces all operations to use an index ID based on the current site. If a method
 * is called with no index ID, one is created by appending the current site name + a separator + a default index
 * suffix. If an index ID is provided, then the actual, final index ID is formed by appending the current site name + a
 * separator + the provided index ID as a suffix.
 *
 * @author avasquez
 */
public class SiteAwareSearchService implements SearchService<Query> {


    protected String indexIdFormat;
    protected SearchService<Query> actualSearchService;

    @Required
    public void setIndexIdFormat(final String indexIdFormat) {
        this.indexIdFormat = indexIdFormat;
    }

    @Required
    public void setActualSearchService(SearchService<Query> actualSearchService) {
        this.actualSearchService = actualSearchService;
    }

    @Override
    public Query createQuery() {
        return actualSearchService.createQuery();
    }

    @Override
    public Query createQuery(Map<String, String[]> params) {
        return actualSearchService.createQuery(params);
    }

    @Override
    public Map<String, Object> search(Query query) throws SearchException {
        return actualSearchService.search(getActualIndexId(null), query);
    }

    @Override
    public Map<String, Object> search(String indexId, Query query) throws SearchException {
        return actualSearchService.search(getActualIndexId(indexId), query);
    }

    @Override
    public void update(String site, String id, String xml, boolean ignoreRootInFieldNames) throws SearchException {
        actualSearchService.update(getActualIndexId(null), site, id, xml, ignoreRootInFieldNames);
    }

    @Override
    public void update(String indexId, String site, String id, String xml,
                         boolean ignoreRootInFieldNames) throws SearchException {
        actualSearchService.update(getActualIndexId(indexId), site, id, xml, ignoreRootInFieldNames);
    }

    @Override
    public void delete(String site, String id) throws SearchException {
        actualSearchService.delete(getActualIndexId(null), site, id);
    }

    @Override
    public void delete(String indexId, String site, String id) throws SearchException {
        actualSearchService.delete(getActualIndexId(indexId), site, id);
    }

    @Override
    public void updateContent(String site, String id, File file) throws SearchException {
        actualSearchService.updateContent(getActualIndexId(null), site, id, file);
    }

    @Override
    public void updateContent(String indexId, String site, String id, File file) throws SearchException {
        actualSearchService.updateContent(getActualIndexId(indexId), site, id, file);
    }

    @Override
    public void updateContent(String site, String id, File file,
                             Map<String, List<String>> additionalFields) throws SearchException {
        actualSearchService.updateContent(getActualIndexId(null), site, id, file, additionalFields);
    }

    @Override
    public void updateContent(String indexId, String site, String id, File file,
                             Map<String, List<String>> additionalFields) throws SearchException {
        actualSearchService.updateContent(getActualIndexId(indexId), site, id, file, additionalFields);
    }

    @Override
    public void updateContent(String site, String id, Content content) throws SearchException {
        actualSearchService.updateContent(getActualIndexId(null), site, id, content);
    }

    @Override
    public void updateContent(String indexId, String site, String id, Content content) throws SearchException {
        actualSearchService.updateContent(getActualIndexId(indexId), site, id, content);
    }

    @Override
    public void updateContent(String site, String id, Content content,
                              Map<String, List<String>> additionalFields) throws SearchException {
        actualSearchService.updateContent(getActualIndexId(null), site, id, content, additionalFields);
    }

    @Override
    public void updateContent(String indexId, String site, String id, Content content,
                              Map<String, List<String>> additionalFields) throws SearchException {
        actualSearchService.updateContent(getActualIndexId(indexId), site, id, content, additionalFields);
    }

    @Override
    public void commit() throws SearchException {
        actualSearchService.commit(getActualIndexId(null));
    }

    @Override
    public void commit(String indexId) throws SearchException {
        actualSearchService.commit(getActualIndexId(indexId));
    }

    protected String getActualIndexId(String indexId) {
        if(StringUtils.isEmpty(indexId)) {
            return String.format(indexIdFormat, getCurrentSiteName());
        } else {
            return String.format(indexIdFormat, indexId);
        }
    }

    protected String getCurrentSiteName() {
        SiteContext siteContext = SiteContext.getCurrent();
        if (siteContext != null) {
            return siteContext.getSiteName();
        } else {
            throw new IllegalStateException("Current site context not found");
        }
    }

}
