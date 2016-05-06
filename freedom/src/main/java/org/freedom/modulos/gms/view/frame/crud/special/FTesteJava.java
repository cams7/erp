package org.freedom.modulos.gms.view.frame.crud.special;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;

public class FTesteJava extends FFilho {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DbConnection con = null;
	
	public FTesteJava() {

		super( false );
		
		setAtribos( 10, 10, 500, 500 );
		
		con = Aplicativo.getInstace().getConexao();
		
		adicBotaoSair();
		
	}

	
}


