package xyz.n8ify.call2

import org.hibernate.dialect.identity.IdentityColumnSupportImpl

class SQLiteIdentityColumnSupport : IdentityColumnSupportImpl() {

    override fun supportsIdentityColumns(): Boolean = true

    override fun getIdentitySelectString(table: String?, column: String?, type: Int): String {
        return "select last_insert_rowid()"
    }

    override fun getIdentityColumnString(type: Int): String = "text"

}