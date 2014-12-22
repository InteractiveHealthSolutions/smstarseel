package smstarseelmodel;

import javax.management.InstanceAlreadyExistsException;

import org.hibernate.Session;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.junit.Test;

public class CallLogTest {

	@Test
	public void testCallLogPerformance() throws InstanceAlreadyExistsException{
		TarseelContext.instantiate(null, "smstarseel.cfg.xml");
		Session ses = TarseelContext.getNewSession();
		System.out.println("START ADDING INDEX");
		ses.beginTransaction();
		long stlwi = System.currentTimeMillis();
		ses.createSQLQuery("CREATE INDEX callstatus_index ON calllog (callstatus);").executeUpdate();
		ses.createSQLQuery("CREATE INDEX calldate_index ON calllog (calldate);").executeUpdate();
		ses.getTransaction().commit();
		System.out.println("END ADDING INDEX (ms): "+(System.currentTimeMillis()-stlwi));

		long stflwi = System.currentTimeMillis();
		ses.beginTransaction();
		ses.createSQLQuery("SELECT * FROM calllog WHERE calldate like '2013-06-30%'").list();
		System.out.println("FETCH TIME WITH INDEXES (ms):"+(System.currentTimeMillis()-stflwi));

		long stulwi = System.currentTimeMillis();
		ses.createSQLQuery("UPDATE calllog SET description = CONCAT('"+System.currentTimeMillis()+"UPDATED NOW ',NOW())").executeUpdate();
		ses.getTransaction().commit();
		System.out.println("UPDATE TIME WITH INDEXES (ms): "+(System.currentTimeMillis()-stulwi));

		try {
			Thread.sleep(1000*10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//---------------------------------------------------
		
		System.out.println("START DROPING INDEX");
		ses.beginTransaction();
		long stl = System.currentTimeMillis();
		ses.createSQLQuery("ALTER TABLE calllog DROP INDEX callstatus_index").executeUpdate();
		ses.createSQLQuery("ALTER TABLE calllog DROP INDEX calldate_index").executeUpdate();
		ses.getTransaction().commit();
		System.out.println("END DROPING INDEX (ms): "+(System.currentTimeMillis()-stl));

		long stfl = System.currentTimeMillis();
		ses.beginTransaction();
		ses.createSQLQuery("SELECT * FROM calllog WHERE calldate like '2013-06-30%'").list();
		System.out.println("FETCH TIME WITHOUT INDEXES (ms):"+(System.currentTimeMillis()-stfl));

		long stul = System.currentTimeMillis();
		ses.createSQLQuery("UPDATE calllog SET description = CONCAT('"+System.currentTimeMillis()+"UPDATED NOW ',NOW())").executeUpdate();
		ses.getTransaction().commit();
		System.out.println("UPDATE TIME WITHOUT INDEXES (ms): "+(System.currentTimeMillis()-stul));
	}
}
