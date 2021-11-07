package xyz.n8ify.call2

import org.hibernate.dialect.Dialect
import org.hibernate.dialect.identity.IdentityColumnSupport
import java.sql.Types

class SQLiteDialect : Dialect() {

    init {
        registerColumnType(Types.BIT, "integer");
        registerColumnType(Types.TINYINT, "tinyint");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.VARCHAR, "text");
        registerColumnType(Types.BOOLEAN, "numeric");
    }

    override fun getIdentityColumnSupport(): IdentityColumnSupport = SQLiteIdentityColumnSupport()

    override fun hasAlterTable(): Boolean = false

    override fun dropConstraints(): Boolean = false

    override fun getDropForeignKeyString(): String = ""

    override fun getAddForeignKeyConstraintString(
        constraintName: String?,
        foreignKey: Array<out String>?,
        referencedTable: String?,
        primaryKey: Array<out String>?,
        referencesPrimaryKey: Boolean
    ): String = ""

    override fun getAddPrimaryKeyConstraintString(constraintName: String?): String = ""

}