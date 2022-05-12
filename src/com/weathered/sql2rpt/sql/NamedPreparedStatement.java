package com.weathered.sql2rpt.sql;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class NamedPreparedStatement {
    private final PreparedStatement statement;
    
    @SuppressWarnings("rawtypes")
	private final Map indexMap;
    
    @SuppressWarnings("rawtypes")
	public NamedPreparedStatement(Connection connection, String query) throws SQLException {
        indexMap=new HashMap();
        String parsedQuery=parse(query, indexMap);
        statement = connection.prepareStatement(parsedQuery);
    }
    
    @SuppressWarnings("rawtypes")
	public NamedPreparedStatement(Connection connection, String query, boolean returnGeneratedKeys) throws SQLException {
        indexMap=new HashMap();
        String parsedQuery=parse(query, indexMap);
        if (returnGeneratedKeys) {
            statement = connection.prepareStatement(parsedQuery, Statement.RETURN_GENERATED_KEYS);
        } else {
            statement = connection.prepareStatement(parsedQuery);
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static final String parse(String query, Map paramMap) {
        int length=query.length();
        StringBuffer parsedQuery=new StringBuffer(length);
        boolean inSingleQuote=false;
        boolean inDoubleQuote=false;
        int index=1;

        for(int i=0;i<length;i++) {
            char c=query.charAt(i);
            if(inSingleQuote) {
                if(c=='\'') {
                    inSingleQuote=false;
                }
            } else if(inDoubleQuote) {
                if(c=='"') {
                    inDoubleQuote=false;
                }
            } else {
                if(c=='\'') {
                    inSingleQuote=true;
                } else if(c=='"') {
                    inDoubleQuote=true;
                } else if(c==':' && i+1<length &&
                        Character.isJavaIdentifierStart(query.charAt(i+1))) {
                    int j=i+2;
                    while(j<length && Character.isJavaIdentifierPart(query.charAt(j))) {
                        j++;
                    }
                    String name=query.substring(i+1,j);
                    c='?';
                    i+=name.length();

                    List indexList=(List)paramMap.get(name);
                    if(indexList==null) {
                        indexList=new LinkedList();
                        paramMap.put(name, indexList);
                    }
                    indexList.add(new Integer(index));

                    index++;
                }
            }
            parsedQuery.append(c);
        }
        
        for(Iterator itr=paramMap.entrySet().iterator(); itr.hasNext();) {
            Map.Entry entry=(Map.Entry)itr.next();
            List list=(List)entry.getValue();
            int[] indexes=new int[list.size()];
            int i=0;
            for(Iterator itr2=list.iterator(); itr2.hasNext();) {
                Integer x=(Integer)itr2.next();
                indexes[i++]=x.intValue();
            }
            entry.setValue(indexes);
        }

        return parsedQuery.toString();
    }
    
    private int[] getIndexes(String name) {
        int[] indexes=(int[])indexMap.get(name);
        if(indexes==null) {
            throw new IllegalArgumentException("Parameter not found: "+name);
        }
        return indexes;
    }
    
    public void setObject(String name, Object value) throws SQLException {
        int[] indexes=getIndexes(name);
        for(int i=0; i < indexes.length; i++) {
            statement.setObject(indexes[i], value);
        }
    }

    public void setNull(String name, int sqlType) throws SQLException {
        int[] indexes=getIndexes(name);
        for (int i = 0; i < indexes.length; i++){
            statement.setNull(indexes[i], sqlType);
        }
    }
    
    public void setString(String name, String value) throws SQLException {
        int[] indexes=getIndexes(name);
        for(int i=0; i < indexes.length; i++) {
            statement.setString(indexes[i], value);
        }
    }
    
    public void setInt(String name, int value) throws SQLException {
        int[] indexes=getIndexes(name);
        for(int i=0; i < indexes.length; i++) {
            statement.setInt(indexes[i], value);
        }
    }
    
    public void setLong(String name, long value) throws SQLException {
        int[] indexes=getIndexes(name);
        for(int i=0; i < indexes.length; i++) {
            statement.setLong(indexes[i], value);
        }
    }
    
    public void setTimestamp(String name, Timestamp value) throws SQLException {
        int[] indexes=getIndexes(name);
        for(int i=0; i < indexes.length; i++) {
            statement.setTimestamp(indexes[i], value);
        }
    }

    public void setDate(String name, Date value) throws SQLException{
        int[] indexes=getIndexes(name);
        for (int index : indexes){
            statement.setDate(index, value);
        }
    }

    public void setTime(String name, Time value) throws SQLException {
        int[] indexes = getIndexes(name);
        for (int index: indexes){
            statement.setTime(index, value);
        }
    }
    
    public PreparedStatement getStatement() {
        return statement;
    }
    
    public boolean execute() throws SQLException {
        return statement.execute();
    }
    
    public ResultSet executeQuery() throws SQLException {
        return statement.executeQuery();
    }
    
    public int executeUpdate() throws SQLException {
        return statement.executeUpdate();
    }
    
    public void close() throws SQLException {
        statement.close();
    }
    
    public void addBatch() throws SQLException {
        statement.addBatch();
    }
    
    public int[] executeBatch() throws SQLException {
        return statement.executeBatch();
    }
}