package act.db;

/*-
 * #%L
 * ACT Framework
 * %%
 * Copyright (C) 2014 - 2017 ActFramework
 * %%
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
 * #L%
 */

import act.Destroyable;

import java.util.Collection;
import java.util.List;

/**
 * The Data Access Object interface
 * @param <ID_TYPE> the generic key type
 * @param <MODEL_TYPE> the generic model type
 */
public interface Dao<ID_TYPE, MODEL_TYPE, QUERY_TYPE extends Query<MODEL_TYPE, QUERY_TYPE>>
        extends Destroyable {

    /**
     * Returns the identifier type
     */
    Class<ID_TYPE> idType();

    /**
     * Returns the class of the Model entity this Dao operates on
     */
    Class<MODEL_TYPE> modelType();

    /**
     * Returns the class of the bounded query type
     */
    Class<QUERY_TYPE> queryType();

    /**
     * Find an entity by id, the primary key
     * @param id the id to find the entity
     * @return the entity found, or {@code null} if not found
     */
    MODEL_TYPE findById(ID_TYPE id);

    /**
     * Find the last created record.
     *
     * @return  the last created record
     */
    MODEL_TYPE findLatest();

    /**
     * Find last modified record.
     *
     * @return last modified record.
     */
    MODEL_TYPE findLastModified();

    /**
     * Find a collection of entities by criteria expression and parameter values.
     *
     * @param expression
     *      the criteria specification
     * @param values
     *      the value array corresponding to the fields specification
     * @return A collection of entities in {@link Iterable}
     * @throws IllegalArgumentException if value number doesn't match the expression requirement
     * @see act.db.util.CriteriaUtil#parse(String, Object...)
     */
    Iterable<MODEL_TYPE> findBy(String expression, Object... values) throws IllegalArgumentException;

    /**
     * Find one entity with fields and values specified.
     *
     * @param expression the fields specification in {@code String}
     * @param values the value array corresponding to the fields specification
     * @return the first entity matches or {@code null} if not found
     * @throws IllegalArgumentException if value number doesn't match the expression requirement
     * @see #findBy(String, Object...)
     * @see act.db.util.CriteriaUtil#parse(String, Object...)
     */
    MODEL_TYPE findOneBy(String expression, Object... values) throws IllegalArgumentException;

    /**
     * Find all entities from a give list of IDs. If there are certain ID in the list does not have
     * an entity associated with then that ID will be ignored. The order of the returned iterator
     * is not defined and shall be implemented as per specific implementation
     * @param idList the ID list specifies the entities shall be returned
     * @return a collection of entities
     */
    Iterable<MODEL_TYPE> findByIdList(Collection<ID_TYPE> idList);

    /**
     * Find all entities of the collection/table specified by {@code MODEL_TYPE}
     * @return all entities of the type bound to this Dao object in {@link Iterable}
     */
    Iterable<MODEL_TYPE> findAll();

    /**
     * Find all entities of the collection/table specified by {@code MODEL_TYPE}
     * @return all entities of the type bound to this Dao object in {@link List}
     */
    List<MODEL_TYPE> findAllAsList();

    /**
     * Reload a model entity from persistent storage by it's {@link ModelBase#_id()}. This method
     * returns the model been reloaded. Depending on the implementation, it could be the model
     * passed in as parameter if it's mutable object or a fresh new object instance with the
     * same ID as the model been passed in.
     *
     * @param entity the model to be reloaded
     * @return a model been reloaded
     */
    MODEL_TYPE reload(MODEL_TYPE entity);

    /**
     * Extract ID value from the give model entity
     * @param entity the model entity object
     * @return the ID of the entity
     */
    ID_TYPE getId(MODEL_TYPE entity);

    /**
     * Returns total number of entities of the model type of this {@code Dao} object.
     */
    long count();

    /**
     * Count the number of entities matches the fields and values specified. For the
     * rule of fields and value specification, please refer to {@link #findBy(String, Object...)}
     * @param fields the fields specification in {@code String}
     * @param values the value array corresponding to the fields specification
     * @return the number of matched entities
     * @throws IllegalArgumentException if fields number and value number doesn't match
     */
    long countBy(String fields, Object ... values) throws IllegalArgumentException;

    /**
     * Save new or update existing the entity in persistent layer with all properties
     * of the entity
     * @param entity the entity to be saved or updated
     * @return the entity that has been saved
     */
    MODEL_TYPE save(MODEL_TYPE entity);

    /**
     * Update existing entity in persistent layer with specified fields and value. This allows
     * partial updates of the entity to save the bandwidth.
     * <p>Note the properties of the entity
     * does not impact the update operation, however the {@link ModelBase#_id()} will be used to
     * locate the record/document in the persistent layer corresponding to this entity.</p>
     * <p>For fields and value specification rule, please refer to {@link #findBy(String, Object...)}</p>
     * @param entity the entity
     * @param fields the fields specification in {@code String}
     * @param values the value array corresponding to the fields specification
     */
    void save(MODEL_TYPE entity, String fields, Object ... values);

    /**
     * Batch save entities
     * @param entities an iterable to get entities to be saved
     */
    List<MODEL_TYPE> save(Iterable<MODEL_TYPE> entities);

    /**
     * Remove the entity specified
     * @param entity the entity to be removed
     */
    void delete(MODEL_TYPE entity);

    /**
     * Remove entities specified by Query
     * @param query the query specifies entities to be removed
     */
    void delete(QUERY_TYPE query);

    /**
     * Remove entity by ID
     * @param id the ID of the entity to be removed
     */
    void deleteById(ID_TYPE id);

    /**
     * Delete a collection of entities by fields and values.
     * <p>The fields is specified in a {@code String} separated by any
     * combination of the following separators</p>
     * <ul>
     *     <li>comma: {@code ,}</li>
     *     <li>[space characters]</li>
     *     <li>semi colon: {@code ;}</li>
     *     <li>colon: {@code :}</li>
     * </ul>
     * <p>The values are specified in an object array. The number of values
     * must match the number of fields specified. Otherwise {@link IllegalArgumentException}
     * will be thrown out</p>
     * <p>If entities found then they are returned in an {@link Iterable}. Otherwise
     * an empty {@link Iterable} will be returned</p>
     * @param fields the fields specification in {@code String}
     * @param values the value array corresponding to the fields specification
     * @throws IllegalArgumentException if fields number and value number doesn't match
     */
    void deleteBy(String fields, Object... values) throws IllegalArgumentException;

    /**
     * Delete all entities in the table/collection inferred by this DAO
     */
    void deleteAll();

    /**
     * Drop all entities (and optionally all indexes) from persistent storage
     */
    void drop();

    /**
     * Return a {@link Query} bound to the {@code MODEL_TYPE}
     * @return an new {@link Query} instance on this Dao
     */
    QUERY_TYPE q();

    /**
     * Alias of {@link #q()}
     */
    QUERY_TYPE createQuery();

    /**
     * Returns a {@link Query} bound to the `MODEL_TYPE` with {@link CriteriaComponent criteria}
     * specified.
     *
     * If there are multiple criteria specified, then they are treated as {@link CriteriaGroupLogic#AND}
     * relationship
     *
     * @param criterion
     *      the first criterion
     * @param criteria
     *      the rest criteria
     * @return
     *      a query instance as described above
     */
    QUERY_TYPE q(CriteriaComponent criterion, CriteriaComponent... criteria);

    /**
     * Alias of {@link #q(CriteriaComponent, CriteriaComponent...)} )}
     */
    QUERY_TYPE createQuery(CriteriaComponent criterion, CriteriaComponent... criteria);

    /**
     * Return a {@link Query} bound to the {@code MODEL_TYPE} by
     * criteria expression and parameter values.
     *
     * If there are multiple criteria in the expression, they are connected
     * by {@link CriteriaGroupLogic#AND and} relationship
     *
     * @param expression
     *      the criteria expression.
     * @param values
     *      the parameter values corresponding to the expression
     * @return the query instance as described above
     * @see act.db.util.CriteriaUtil#parse(String, Object...)
     */
    QUERY_TYPE q(String expression, Object... values);

    /**
     * Alias of {@link #q(String, Object...)}
     */
    QUERY_TYPE createQuery(String expression, Object... values);

    /**
     * Return a {@link Query} bound to the {@code MODEL_TYPE} by
     * criteria expression and parameter values.
     *
     * If there are multiple criteria in the expression, they are connected
     * by {@link CriteriaGroupLogic#OR or} relationship
     *
     * @param expression
     *      the criteria expression.
     * @param values
     *      the parameter values corresponding to the expression
     * @return the query instance as described above
     * @see act.db.util.CriteriaUtil#parse(String, Object...)
     */
    QUERY_TYPE or(String expression, Object... values);

    /**
     * Alias of {@link #or(String, Object...)}
     */
    QUERY_TYPE createOrQuery(String expression, Object... values);

    /**
     * Returns a {@link Query} bound to the `MODEL_TYPE` with {@link CriteriaComponent criteria}
     * specified.
     *
     * If there are multiple criteria specified, then they are treated as {@link CriteriaGroupLogic#OR}
     * relationship.
     *
     * @param criterion
     *      the first criterion
     * @param criteria
     *      the rest criteria
     * @return
     *      a query instance as described above
     */
    QUERY_TYPE or(CriteriaComponent criterion, CriteriaComponent... criteria);

    /**
     * Alias of {@link #or(CriteriaComponent, CriteriaComponent...)}
     */
    QUERY_TYPE createOrQuery(CriteriaComponent criterion, CriteriaComponent... criteria);

    /**
     * Return an object that could be a SQL LIKE value from original target string `v`.
     *
     * E.g. for SQL type database it might check if the v has `%` inside, if not then
     * wrap with with `%`.
     *
     * @param v the target value string
     * @return a target object subject to SQL LIKE operation.
     */
    Object processLikeValue(String v);

}
