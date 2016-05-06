package org.freedom.infra.model.jpa;


public class Crud {
/*
	public static final int PERSIST_FRAMEWORK_TOPLINK = 1;

	public static final int DEFAULT_CHANGES = 5;

	private static final int ERRO_CODE_DB2_NOT_CONNECT = -99999;

	private final String persistUnit;

	private final Map<String, String> properties;

//	private EntityManagerFactory managerFactory = null;

//	private EntityManager manager = null;

	//private EntityTransaction transaction = null;

	private Logger logger = null;

	private int changes = DEFAULT_CHANGES;

	public Crud(final String persistUnit) throws Exception {

		this(persistUnit, null, false);
	}

	public Crud(final String persistUnit, final boolean writerLogger) throws Exception {

		this(persistUnit, null, writerLogger);
	}

	public Crud(final String persistUnit, final Map<String, String> properties) throws Exception {

		this(persistUnit, properties, false);
	}

	public Crud(final String persistUnit, final Map<String, String> properties, final boolean writerLogger) throws Exception {

		this.persistUnit = persistUnit;
		this.properties = properties;

		if (writerLogger) {
			if (properties == null) {
				logger = FreedomLogger.getLogger(this.getClass(), null, FreedomLogger.LOGGER_JPA);
			}
			else {
				logger = FreedomLogger.getLogger(this.getClass(), properties.get("initFile"), FreedomLogger.LOGGER_JPA);
			}
		}

		connect();
	}

	private void connect() throws Exception {

		if (properties == null) {
			managerFactory = Persistence.createEntityManagerFactory(persistUnit);
		}
		else {
			managerFactory = Persistence.createEntityManagerFactory(persistUnit, properties);
		}
		manager = managerFactory.createEntityManager();
		transaction = manager.getTransaction();
		
		
	}

	protected boolean reConnect(final Throwable exception) {

		try {
			if (exception instanceof DatabaseException) {
				DatabaseException dbex = ( DatabaseException ) exception;
				if (dbex.getInternalException() instanceof SQLException) {
					SQLException sqlex = ( SQLException ) dbex.getInternalException();
					if (isErroNotConnect(sqlex.getErrorCode())) {
						dbex.getAccessor().reestablishConnection(dbex.getSession());
					}
				}
			}
		}
		catch (DatabaseException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public Crud(final String persistUnit, final int persistFramework, final String initFile, final String sessionName, final String userParam, final String passwordParam) throws Exception {

		this(persistUnit, persistFramework, initFile, sessionName, userParam, passwordParam, false);
	}

	public Crud(final String persistUnit, final int persistFramework, final String initFile, final String sessionName, final String userParam, final String passwordParam, final boolean writerLogger)
			throws Exception {

		this(persistUnit, getProperties(persistFramework, initFile, sessionName, userParam, passwordParam), writerLogger);
	}

	private static Map<String, String> getProperties(final int persistFramework, final String initFile, final String sessionName, final String userParam, final String passwordParam) throws Exception {

		Map<String, String> properties = new HashMap<String, String>();
		properties.put("initFile", initFile);

		String username = null;
		String password = null;

		ManagerIni ini = ManagerIni.createManagerIniFile(initFile);
		username = ini.getProperty(sessionName, userParam);
		password = SimpleCrypt.decrypt(ini.getProperty(sessionName, passwordParam));

		if (persistFramework == PERSIST_FRAMEWORK_TOPLINK) {
			properties.put("toplink.jdbc.user", username);
			properties.put("toplink.jdbc.password", password);
		}

		return properties;
	}

	private boolean isErroNotConnect(int codeError) {

		if (codeError == ERRO_CODE_DB2_NOT_CONNECT) {
			return true;
		}

		return false;
	}

	protected EntityTransaction getTransaction() {
		return this.transaction;
	}

	protected EntityManager getManager() {
		return this.manager;
	}

	public int getChanges() {
		return changes;
	}

	public void setChanges(int changes) {
		this.changes = changes;
	}

	protected void writerError(final Throwable e) {

		if (logger != null) {
			logger.error(e.getMessage(), e);
		}
	}

	public void persist(final PersistObject entity, final boolean repeat) throws NotConnectionCrudException {

		int changes = this.changes;
		boolean repeatConnect = true;

		while (changes-- > 0 && repeatConnect) {

			try {

				repeatConnect = repeat;

				// transaction = manager.getTransaction();
				transaction.begin();

				Key keyback = entity.getKey();
				PersistObject tmp = manager.merge(entity);
				tmp.setKey(keyback);

				manager.persist(tmp);

				transaction.commit();

				changes = 0;
			}
			catch (DatabaseException e) {
				writerError(e);
				transaction.rollback();
				DatabaseException dbex = ( DatabaseException ) e;
				if (dbex.getInternalException() instanceof SQLException) {
					SQLException sqlex = ( SQLException ) dbex.getInternalException();
					if (isErroNotConnect(sqlex.getErrorCode()) && changes == 0) {
						throw new NotConnectionCrudException("Not connection for persist.", e);
					}
				}
			}
			catch (Exception e) {
				writerError(e);
				transaction.rollback();
				changes = 0;
			}
		}
	}

	public void remove(PersistObject entity, final boolean repeat) throws NotConnectionCrudException {

		int changes = this.changes;
		boolean repeatConnect = true;

		while (changes-- > 0 && repeatConnect) {

			try {

				repeatConnect = repeat;

				Object tmp = manager.find(entity.getClass(), ( ( PersistObject ) entity ).getKey());

				// transaction = manager.getTransaction();
				transaction.begin();

				manager.remove(tmp);

				transaction.commit();

				changes = 0;
			}
			catch (DatabaseException e) {
				writerError(e);
				transaction.rollback();
				DatabaseException dbex = ( DatabaseException ) e;
				if (dbex.getInternalException() instanceof SQLException) {
					SQLException sqlex = ( SQLException ) dbex.getInternalException();
					if (isErroNotConnect(sqlex.getErrorCode()) && changes == 0) {
						throw new NotConnectionCrudException("Not connection for remove.", e);
					}
				}
			}
			catch (Exception e) {
				writerError(e);
				transaction.rollback();
				changes = 0;
			}
		}
	}

	public PersistObject find(final Class<? extends PersistObject> clas, final Key key, final boolean repeat) throws NotConnectionCrudException {

		PersistObject object = null;

		int changes = this.changes;
		boolean repeatConnect = true;

		while (changes-- > 0 && repeatConnect) {

			try {

				repeatConnect = repeat;

				// transaction = manager.getTransaction();
				transaction.begin();
				Key keyback = key;
				object = manager.find(clas, key);
				object.setKey(keyback);
				// REVISAR
				// SEM ESTE, NÃO ESTAVA ATUALIZANDO.
				manager.refresh(object);
				transaction.commit();

				changes = 0;
			}
			catch (DatabaseException e) {
				writerError(e);
				transaction.rollback();
				DatabaseException dbex = ( DatabaseException ) e;
				if (dbex.getInternalException() instanceof SQLException) {
					SQLException sqlex = ( SQLException ) dbex.getInternalException();
					if (isErroNotConnect(sqlex.getErrorCode()) && changes == 0) {
						throw new NotConnectionCrudException("Not connection for find.", e);
					}
				}
			}
			catch (Exception e) {
				writerError(e);
				transaction.rollback();
				changes = 0;
			}
		}

		return object;
	}

	public PersistObject find(final PersistObject persistObject, final boolean repeat) throws NotConnectionCrudException {

		PersistObject object = null;
		if (persistObject != null) {
			object = find(persistObject.getClass(), persistObject.getKey(), repeat);
		}
		return object;
	}

	public Query createQuery(String sql) {
		return manager.createQuery(sql);
	}

	public Query createNamedQuery(String sql) {
		return manager.createNamedQuery(sql);
	}

	public Query createNativeQuery(String sql) {
		return manager.createNativeQuery(sql);
	}

	public List<?> executeQuery(final String sql, final Map<String, Object> properties, final boolean repeat) throws Exception {

		Query query = createQuery(sql);

		for (String key : properties.keySet()) {
			query.setParameter(key, properties.get(key));
		}

		return executeQuery(query, repeat);
	}

	public List<?> executeNamedQuery(final String sql, final boolean repeat) throws Exception {
		return executeQuery(createNamedQuery(sql), repeat);
	}

	public List<?> executeNamedQuery(final String sql, final Map<String, Object> properties, final boolean repeat) throws Exception {

		Query query = createNamedQuery(sql);

		for (String key : properties.keySet()) {
			query.setParameter(key, properties.get(key));
		}

		return executeQuery(query, repeat);
	}

	public List<?> executeQuery(final String sql, final boolean repeat) throws Exception {
		return executeQuery(createQuery(sql), repeat);
	}

	public List<?> executeNativeQuery(final String sql, final Map<Object, Object> properties, final boolean repeat) throws Exception {

		Query query = createNativeQuery(sql);

		for (Object key : properties.keySet()) {
			if (key instanceof Integer) {
				query.setParameter(( Integer ) key, properties.get(key));
			}
			else if (key instanceof String) {
				query.setParameter(( String ) key, properties.get(key));
			}
		}

		return executeQuery(query, repeat);
	}

	public List<?> executeNativeQuery(final String sql, final boolean repeat) throws Exception {
		return executeQuery(createNativeQuery(sql), repeat);
	}

	public List<?> executeQuery(final Query query, final boolean repeat) throws Exception {

		List<?> list = null;

		int changes = this.changes;
		boolean repeatConnect = true;

		while (changes-- > 0 && repeatConnect) {

			try {

				repeatConnect = repeat;
				transaction.begin();
				list = query.getResultList();
				transaction.commit();

				changes = 0;
			}
			catch (DatabaseException e) {
				writerError(e);
				transaction.rollback();
				DatabaseException dbex = ( DatabaseException ) e;
				if (dbex.getInternalException() instanceof SQLException) {
					SQLException sqlex = ( SQLException ) dbex.getInternalException();
					if (isErroNotConnect(sqlex.getErrorCode()) && changes == 0) {
						throw new NotConnectionCrudException("Not connection for find.", e);
					}
				}
			}
			catch (Exception e) {
				writerError(e);
				transaction.rollback();
				changes = 0;
			}
		}

		return list;
	}
	
	*/
}
