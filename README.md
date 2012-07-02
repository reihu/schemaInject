schemaInject
============

This project aims to provide a way to automatically create your database schema independent to which database you use.
It will also help you with schema updates.

Requirements
------------

 * Java
 * A database server (we're using PostgreSQL and SQLite)
 * ...
 * Apache Maven (optional)

Usage
-----

 * Create an xml database schema file. How to do that is specified in the _Schema file/folder_ chapter
 * call `new SchemaInject(db, "path/to/schema.xml").go();

schemaInject will then do the following steps:
 * check if te schema file is valid (and doesn't contain any duplicate 
 * Look if the database is empty. If it's empty, it fills it according to the schema declaration.
 * If the database wasn't empty, schemaInject looks for a metaTable (default: `_schemaInject`) and
   reads the revision information (it either uses the revision attribute if specified or uses the
   md5 checksum of the schema file) to check if the schema has changed
 * If the two values are equal (i.e. `schema_xml.revision == db.revision` or `schema_xml.md5 == db.storedMd5`),
   schemaInject is done.
 * If they differ, schemaInject reads the database schema (i.e. all the tables and indexes) and compares
   them to the provided schema.xml and...
   * creates every table, field and index that wasn't found in the database
   * fills fields with the value of any `oldname` elements specified for them in the order they are specified (if there are any)
   * if `nodelete` is false removes those that haven't been found
   * if `nodelete` is true (default)
     * prints a warning for each unknown table, but doesn't touch them (as they may contain valuable data)
     * sets unknown fields to `NULL` (so that they may contain NULL values)
     * deletes all unknown indices (as they don't affect the data itself)
     You can nevertheless set the `delete` attribute to true for fields and tables you want to delete explicitly.

schemaInject...
 * won't do anything unless it can read all of the specified schema files
 * also refuses to work if it found no tables in the schema.xml
 * will check if the schema.xml is valid before it does anything
 * will encapsulate the whole schema update into a transaction for database engines that support schema transactions (e.g. PostgreSQL)
 * only acts on tables that have the given `prefix`. As this prefix defaults
   to `""`, all tables will be used unless a prefix is specified explicitly
 * won't delete any data unless you explicitly set `nodelete` to false.
 * creates sequences implicitly. However there are ways to create/delete them
   explicitly.
   They are deleted implicitly only when `nodelete="false"` and they aren't referenced anymore.
   You can explicitly delete one by specifying `delete="true"` in its explicit
   `<sequence>` declaration.
   But keep in mind that if the sequence is in use when you want to delete it,
   schemaInject will refuse to work as the schema declaration will be
   considered invalid.
 * can be used with ORMs like jOOQ that don't modify the database schema
 * likes SQL
 * needs a better name. Suggest one at https://github.com/reihu/schemaInject/issues/1


Example schema
--------------

	<schema revision="1" nodelete="true" metaTable="_schemaInject" prefix="demo-" >
		<!-- Sequences come first -->
		<sequence name="seq_foo" start="10" interval="1" delete="false" />
		<!-- Then the tables -->
		<table name="user">
			<field name="uid" type="integer" sequence="seq_user_uid" null="false" />
			<field name="login" type="varchar[50]" null="false" />
			<field name="password" type="char[32]" null="false">
				<oldname>pwd</oldname>
			</field>

			<pkey field="uid" />
		</table>
		<table name="session" comment="Session table">
			<field name="sid" type="char[32]" null="false" />
			<field name="uid" type="integer" null="false" />
			<field name="lastActive" type="timestamp" null="false" default="%NOW%" />
			<field name="deletedField" delete="true" />

			<pkey field="sid" type="" />
			<fkey field="uid" refTable="user" refField="uid" />
		</table>
		<table name="foo" ignore="true" />
		<table name="deleteMe" delete="true" />
	</schema>

   The schema contains the following items

   * The `<schema>` element is the root element of the document.

     It may contain the following attributes
     * `metaTable` to indicate the name of the schemaInject meta table
       (defaults to `_schemaInject`)
     * `nodelete` specifies whether schemaInject is allowed to delete data.
     * `revision` to specify the revision of the current schema.
     * `prefix` (default: "") to specify a prefix for all managed tables

     It contains a `<table>` element for each database table that should be
     created and may contain several explicit sequence declarations
   * The `<sequence>` element is used to explicitly specify a sequence.
     It supports the following attributes:
     * `name` (required): Sequence name
     * `start` (optional): starting value (if supported by the database)
     * `interval` (optional): stepping interval (if supported by the database)
     * `delete` (optional): set this to true to explicitly delete a sequence
   * The `<table>` element has the following attributes:
     * `name` (required): The name of the table
     * `comment`: optional comment
     * `ignore`: set this to true to completely ignore a table (use this for external tables)
     * `delete`: completely remove the table and its content

     It can contain `<field>`, `<pkey>`, `<fkey>` and `<unique>` elements.
   * The `<field>` element as the following attributes:
     * `name` (required): the name of the field
     * `type` (required if none of `ignore` and `delete` are specified): field type
     * `null` (default: `false`): Specifies wheter the field accepts NULL values
     * `sequence` (default: ""): Use a sequence on this field. Sequences are created implicitly
     * `ignore` (default: `false`): Completely ignores the field
     * `delete` (default: `false`): Deletes the field
   * The `<pkey>` element specifies the table's primary key.

     It is either empty and has one `field` attribute or contains several
     `<field>fieldName</field>` subelements (for pkeys over more than one field)
   * The `<unique>` element creates a unique constraint.
     It is either empty and has one `field` attribute or contains several
     `<field>fieldName</field>` subelements (for unique indexes over more than
     one field)
   * The `<fkey>` element always has a `refTable` attribute and either a
     `field` and `refField` for single-field fkeys or several
     `<field name="localFieldName" refField="remoteFieldName" />` subelements.

     It may contain the following additional attributes:
     * `deferred` (default: "false"), other values: "deferred", "immediate" )
       can be used for deferred foreign keys (that will be checked at the end
       of a transaction). The possible values are:
       * `false` the foreign key is NOT deferrable
       * `deferred` deferrable and initially deferred
       * `immediate` deferrable but initially immediate
   * TODO: add support for plain indices
 * __A schema folder__

   _to be specified..._

Example code
------------
_to be specified_

License
-------
schemaInject is released under the terms of the Apache License 2 which can be found at:
http://www.apache.org/licenses/LICENSE-2.0.html
