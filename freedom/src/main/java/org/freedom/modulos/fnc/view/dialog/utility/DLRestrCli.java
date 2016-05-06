package org.freedom.modulos.fnc.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

public class DLRestrCli extends FFDialogo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1042635082152391826L;

	private static boolean OK = true;

	private static List<?> listrestr = null;

	private JTablePad tab = new JTablePad();

	private final JPanelPad pnTab = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JScrollPane spn = new JScrollPane( tab );

	public DLRestrCli( final Component cOrig ) {

		super( cOrig );
		setModal( true );
		montaTela();
	}

	private void montaTela() {

		setTitulo( "Restrições do cliente" );
		setAtribos( 600, 400 );
		pnTab.add( spn, BorderLayout.CENTER );
		c.add( pnTab, BorderLayout.CENTER );
		tab.adicColuna( "Data" );
		tab.adicColuna( "Tipo de restrição" );
		tab.adicColuna( "Observação" );
		tab.setTamColuna( 80, 0 );
		tab.setTamColuna( 200, 1 );
		tab.setTamColuna( 300, 2 );
	}

	public static boolean execRestrCli( final Component cOrig, final DbConnection cn, final int codcli ) {

		listrestr = carregaRestricao( cn, codcli );
		if ( listrestr.size() > 0 ) {
			DLRestrCli dl = new DLRestrCli( cOrig );
			dl.setConexao( cn );
			dl.carregaLista();
			dl.setVisible( true );
		}
		return OK;
	}

	private static List<?> carregaRestricao( final DbConnection cn, final int codcli ) {

		List<String[]> list = new ArrayList<String[]>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		String[] row = null;
		try {
			sql.append( "SELECT R.DTRESTR, TR.DESCTPRESTR, R.OBSRESTR " );
			sql.append( "FROM FNRESTRICAO R, FNTIPORESTR TR " );
			sql.append( "WHERE R.CODEMP=? AND R.CODFILIAL=? AND " );
			sql.append( "R.CODCLI=? AND R.SITRESTR IN ('I') AND " );
			sql.append( "TR.CODEMP=R.CODEMPTR AND TR.CODFILIAL=R.CODFILIALTR AND " );
			sql.append( "TR.CODTPRESTR=R.CODTPRESTR " );
			sql.append( "ORDER BY DTRESTR DESC " );
			ps = cn.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNRESTRICAO" ) );
			ps.setInt( 3, codcli );
			rs = ps.executeQuery();
			while ( rs.next() ) {
				row = new String[ 3 ];
				row[ 0 ] = Funcoes.dateToStrDate( rs.getDate( "DTRESTR" ) );
				row[ 1 ] = limpaString( rs.getString( "DESCTPRESTR" ) );
				row[ 2 ] = limpaString( rs.getString( "OBSRESTR" ) );
				list.add( row );
			}
			cn.commit();
			rs.close();
			ps.close();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return list;
	}

	private static String limpaString( final String text ) {

		if ( text == null ) {
			return "";
		}
		else {
			return text.trim();
		}
	}

	public void setConexao( DbConnection cn ) {

		con = cn;
	}

	public void carregaLista() {

		Vector<Object> row = null;
		for ( int i = 0; i < listrestr.size(); i++ ) {
			row = new Vector<Object>();
			row.addElement( ( (String[]) listrestr.get( i ) )[ 0 ] );
			row.addElement( ( (String[]) listrestr.get( i ) )[ 1 ] );
			row.addElement( ( (String[]) listrestr.get( i ) )[ 2 ] );
			tab.adicLinha( row );
		}

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			OK = true;
		}
		else {
			OK = false;
		}
		super.actionPerformed( evt );

	}

}
