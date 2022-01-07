package fr.umontpellier.dao;

import packageTestHibernate.Monument;

public interface IMonumentDAO {
	boolean save(Monument monument)throws Exception;

}
