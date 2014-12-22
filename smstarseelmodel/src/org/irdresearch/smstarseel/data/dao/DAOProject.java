package org.irdresearch.smstarseel.data.dao;

import java.util.List;

import org.irdresearch.smstarseel.data.Project;

public interface DAOProject extends DAO{
	
	Number LAST_QUERY_TOTAL_ROW__COUNT();

	Project findById(int id);
	
	List<Project> findByCriteria(String projectname);

	List<Project> getAll(int firstResult, int fetchsize);
}
