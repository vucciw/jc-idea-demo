package com.jc.idea.db;

import java.sql.Connection;

public interface DataSource {

   Connection getConnection() ;
}
