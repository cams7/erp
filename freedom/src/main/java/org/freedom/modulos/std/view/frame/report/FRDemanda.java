/**
 * @version 23/11/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva e Robson Sanchez<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRDemanda.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * <BR>
 * 
 *         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRDemanda extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodMarca = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldPad txtDescMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtSiglaMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JLabelPad lbCodGrup = new JLabelPad( "Cód.grupo" );

	private JLabelPad lbCodMarca = new JLabelPad( "Cód.marca" );

	private JLabelPad lbDescGrup = new JLabelPad( "Descrição do grupo" );

	private JLabelPad lbDescMarca = new JLabelPad( "Descrição do marca" );

	private JRadioGroup<?, ?> rgOrdem = null;

	private Vector<String> vLabs = new Vector<String>( 2 );

	private Vector<String> vVals = new Vector<String>( 2 );
	
	private JRadioGroup<String, String> rgTipo = null;

	private Vector<String> vLabsGraf = new Vector<String>( 2 );

	private Vector<String> vValsGraf = new Vector<String>( 2 ) ;
	
	private JRadioGroup<String, String> rgTipoDeRelatorio = null;
	
	private Vector<String> vLabsRel = new Vector<String>();
	
	private Vector<String> vValsRel = new Vector<String>();

	private ListaCampos lcGrup = new ListaCampos( this );

	private ListaCampos lcMarca = new ListaCampos( this );

	private JCheckBoxPad cbGrupo = new JCheckBoxPad( "Dividir por grupo.", "S", "N" );

	private JCheckBoxPad cbSemMovSaldoIni = new JCheckBoxPad( "Com saldo inicial.", "S", "N" );

	private JCheckBoxPad cbSemMovEnt = new JCheckBoxPad( "Com movimentação de entrada.", "S", "N" );

	private JCheckBoxPad cbSemMovSaida = new JCheckBoxPad( "Com movimentação de saída.", "S", "N" );

	private JCheckBoxPad cbSemMovSaldo = new JCheckBoxPad( "Com saldo.", "S", "N" );

	public FRDemanda() {

		setTitulo( "Relatório de Demanda" );
		setAtribos( 140, 40, 348, 410 );
		vLabs.addElement( "Código" );
		vLabs.addElement( "Descrição" );
		vLabs.addElement( "+ Vendido" );
		vVals.addElement( "C" );
		vVals.addElement( "D" );
		vVals.addElement( "M" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "D" );
		
		vLabsGraf.addElement( "Gráfico" );
		vLabsGraf.addElement( "Texto" );
		vValsGraf.addElement( "G" );
		vValsGraf.addElement( "T" );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabsGraf, vValsGraf );
		rgTipo.setVlrString( "T" );

		vLabsRel.addElement( "Retrato" );
		vLabsRel.addElement( "Paisagem" );
		vValsRel.addElement( "R" );
		vValsRel.addElement( "P" );

		rgTipoDeRelatorio = new JRadioGroup<String, String>( 1, 2, vLabsRel, vValsRel );
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.add( Calendar.DATE, -30 );
		txtDataini.setVlrDate( cal.getTime() );
		cal.add( Calendar.DATE, 30 );
		txtDatafim.setVlrDate( cal.getTime() );
		txtDataini.setRequerido( true );
		txtDatafim.setRequerido( true );

		lcMarca.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca", ListaCampos.DB_PK, false ) );
		lcMarca.add( new GuardaCampo( txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false ) );
		lcMarca.add( new GuardaCampo( txtSiglaMarca, "SiglaMarca", "Sigla", ListaCampos.DB_SI, false ) );
		lcMarca.montaSql( false, "MARCA", "EQ" );
		lcMarca.setReadOnly( true );
		txtCodMarca.setTabelaExterna( lcMarca, null );
		txtCodMarca.setFK( true );
		txtCodMarca.setNomeCampo( "CodMarca" );

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrup.montaSql( false, "GRUPO", "EQ" );
		lcGrup.setReadOnly( true );
		txtCodGrup.setTabelaExterna( lcGrup, null );
		txtCodGrup.setFK( true );
		txtCodGrup.setNomeCampo( "CodGrup" );

		adic( new JLabelPad( "Periodo" ), 7, 0, 250, 20 );
		adic( txtDataini, 7, 20, 100, 20 );
		adic( txtDatafim, 110, 20, 100, 20 );

		adic( lbCodMarca, 7, 40, 250, 20 );
		adic( txtCodMarca, 7, 60, 80, 20 );
		adic( lbDescMarca, 90, 40, 250, 20 );
		adic( txtDescMarca, 90, 60, 197, 20 );
		adic( lbCodGrup, 7, 80, 250, 20 );
		adic( txtCodGrup, 7, 100, 80, 20 );
		adic( lbDescGrup, 90, 80, 250, 20 );
		adic( txtDescGrup, 90, 100, 197, 20 );
		adic( rgOrdem, 7, 130, 300, 30 );
		adic( rgTipo, 7, 163, 300, 30 );
		adic( rgTipoDeRelatorio, 7,196, 300, 30);
		adic( cbGrupo, 7, 230, 250, 20 );
		adic( cbSemMovSaldoIni, 7, 250, 250, 20 );
		adic( cbSemMovEnt, 7, 270, 250, 20 );
		adic( cbSemMovSaida, 7, 290, 250, 20 );
		adic( cbSemMovSaldo, 7, 310, 250, 20 );
	}

	private boolean comRef() {

		boolean bRetorno = false;
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
					bRetorno = true;
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		}
		return bRetorno;
	}
	
	public void imprimir( TYPE_PRINT bVisualizar ) {
		
		Blob fotoemp = null;
		
		
		if ( "G".equals(rgTipo.getVlrString()) ) {
			try {
				PreparedStatement ps = con.prepareStatement( "SELECT FOTOEMP FROM SGEMPRESA WHERE CODEMP=?" );
				ps.setInt( 1, Aplicativo.iCodEmp );
				
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					fotoemp = rs.getBlob( "FOTOEMP" );
				}
				rs.close();
				ps.close();
				con.commit();
				
			} catch (Exception e) {
				Funcoes.mensagemErro( this, "Erro carregando logotipo.\n" + e.getMessage() );
				e.printStackTrace();
			}						
		}
		
		imprimiDemanda( bVisualizar, "G".equals(rgTipo.getVlrString()) , fotoemp );
				
	}

	public void imprimiDemanda( TYPE_PRINT bVisualizar, boolean postscript  , Blob fotoemp ) {
		
		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		
		StringBuilder sSQL = new StringBuilder();
		StringBuilder sWhere = new StringBuilder();
		StringBuilder sWhereTM = new StringBuilder();
		String sDivGrupo = "";
		String sOrdem = rgOrdem.getVlrString();
		String sCampo = "";
		String sCab = "";
		String sOrdenado = "";
		String sOrdemGrupo = "";
		String sCodgrup = "";

		boolean bComRef = false;

		if ( txtDataini.getVlrString().length() < 10 || txtDatafim.getVlrString().length() < 10 ) {
			Funcoes.mensagemInforma( this, "Período inválido!" );
			return;
		}

		sDivGrupo = cbGrupo.getVlrString();

		if ( sDivGrupo.equals( "S" ) ) {
			sOrdemGrupo = "P.CODGRUP,";
		}
		else {
			sOrdemGrupo = "";
		}

		bComRef = comRef();
		sCampo = bComRef ? "REFPROD" : "CODPROD";

		if ( sOrdem.equals( "C" ) ) {

			if ( bComRef ) {
				sOrdem = sOrdemGrupo + "P.REFPROD";
				sOrdenado = "ORDENADO POR REFERENCIA";
			}
			else {
				sOrdem = sOrdemGrupo + "P.CODPROD";
				sOrdenado = "ORDENADO POR CODIGO";
			}
		}

		if ( sOrdem.equals( "D" ) ) {
			sOrdem = sOrdemGrupo + "P.DESCPROD";
			sOrdenado = "ORDENADO POR DESCRICAO";
		}

		if ( sOrdem.equals( "M" ) ) {
			sOrdem = sOrdemGrupo + "(P.VLRVENDAS + P.VLRDEVENT + P.VLROUTSAI)" + " DESC" + ",P.DESCPROD ";
			sOrdenado = "ORDENADO POR MAIS VENDIDOS";
		}

		if ( "S".equals( cbSemMovSaldoIni.getVlrString() ) ) {
			if ( sWhereTM.length() > 0 ){  // NAO FOR VAZIO
				sWhereTM.append( " OR " );
			}
			sWhereTM.append( " p.sldini>0" );
		}

		if ( "S".equals( cbSemMovEnt.getVlrString() ) ) {
			if ( sWhereTM.length() > 0 )
				sWhereTM.append( " OR " );
			sWhereTM.append( "(P.VLRCOMPRAS + P.VLRDEVSAI + P.VLROUTENT) > 0" );
		}

		if ( "S".equals( cbSemMovSaida.getVlrString() ) ) {
			if ( sWhereTM.length() > 0 ){
				sWhereTM.append( " OR " );
			}
			sWhereTM.append( " (P.VLRVENDAS + P.VLRDEVENT + P.VLROUTSAI) > 0" );
		}

		if ( "S".equals( cbSemMovSaldo.getVlrString() ) ) {
			if ( sWhereTM.length() > 0 ){
				sWhereTM.append( " OR " );
			}
			sWhereTM.append( " p.sldfim>0 " );
		}


		/*
		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		*/

		if ( txtCodMarca.getText().trim().length() > 0 ) {
			sWhereTM.append( " P.CODMARCA = '" + txtCodMarca.getText().trim() + "'" );
			
		}

		if ( txtCodGrup.getText().trim().length() > 0 ) {

			/**
			 * @version 08/01/2008 <BR>
			 *          Implementação realizada por
			 * @author Setpoint Informática Ltda./Diego Manoel (diego.manoel@gmail.com) <BR>
			 *         Revisado e "commitado" por Anderson Sanchez (Setpoint Informática Ltda)
			 */

			if ( sWhereTM.length() > 0 ) {
				sWhereTM.append( " AND " ); 
			}

			/****************************************/

			sWhereTM.append( " P.CODGRUP LIKE '" + txtCodGrup.getText().trim() + "%'" );
		}

		if ( sWhereTM.length() > 0 )
			sWhere.append( " WHERE " + sWhereTM );

		  sSQL.append( " SELECT P.CODMARCA, P.CODGRUP,P.CODPROD, " ); 
		  sSQL.append( "P.REFPROD, P.DESCPROD, P.DESCGRUP," );
		  sSQL.append( "P.SLDINI, P.VLRCOMPRAS, P.VLRDEVENT, P.VLROUTENT, " ); 
		  sSQL.append( "P.VLRVENDAS, P.VLRDEVSAI, P.VLROUTSAI, P.SLDFIM " ); 
		  sSQL.append( "FROM EQRELDEMANDASP (?, ?, ?, ?, ?) P " );
		  sSQL.append( sWhere );
		  sSQL.append( " ORDER BY " ); 
		  sSQL.append( sOrdem );

		try {

			System.out.println("SQL:" + sSQL.toString());
			
			PreparedStatement ps = con.prepareStatement( sSQL.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQMOVPROD" ) );
			ps.setInt( 3, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 5, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ResultSet rs = ps.executeQuery();
	
	
			if (postscript) {
				imprimiGrafico( bVisualizar, sCab, rs, fotoemp );
			} else {
				imprimiTexto( bVisualizar, sCab, rs, sCodgrup, sOrdenado, sDivGrupo, sCampo );
				rs.close();
				ps.close();
				con.commit();			
			}
			
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta de estoque!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}


	}
	
	private void imprimiGrafico( TYPE_PRINT bVisualizar, 
			String sCab, ResultSet rs, Blob fotoemp ) {
		
		String report = null;
		String label = null;
		String jasper = null;
		String sufixo = null;
		
		//String report = "relatorios/FRDemanda.jasper";
		//String label = "Demanda";
		
	    HashMap<String, Object> hParam = new HashMap<String, Object>();
		//hParam.put( "FILTROS", sFiltros1 + "FILTROS "+ sFiltros2 );
		try {
			if ("P".equals( rgTipoDeRelatorio.getVlrString() )) {
				sufixo = "_Landscape.jasper";
			} else {
				sufixo = ".jasper";
			}
			if ( "S".equals( cbGrupo.getVlrString() )){
				jasper = "FRDemandaGrupo";
				label = "Demanda dividida por grupo";
			} else {
				jasper = "FRDemanda";
				label = "Demanda";
			}
			report = "relatorios/"+jasper+sufixo;
			hParam.put( "LOGOEMP",  new ImageIcon(fotoemp.getBytes(1, ( int ) fotoemp.length())).getImage() );
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro carregando logotipo !\n" + e.getMessage()  );
			e.printStackTrace();
		}
		
		FPrinterJob dlGr = new FPrinterJob( report , label, sCab, rs, hParam , this );
		
		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		} else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de Demanda!" + err.getMessage(), true, con, err );
			}
		}
	
	}
	
	public void imprimiTexto( TYPE_PRINT bVisualizar, String sCab, ResultSet rs, String sCodgrup, String sOrdenado, String sDivGrupo, String sCampo ){
		
		sOrdenado = "|" + StringFunctions.replicate( " ", 67 - ( sOrdenado.length() / 2 ) ) + sOrdenado;
		sOrdenado += StringFunctions.replicate( " ", 133 - sOrdenado.length() ) + " |";
		ImprimeOS imp = new ImprimeOS( "", con );
		
		int linPag = imp.verifLinPag() - 1;
		try {
		if ( txtCodMarca.getText().trim().length() > 0 ) {
			String sTmp = "MARCA: " + txtDescMarca.getText().trim();
			sCab += "\n" + imp.comprimido();
			sTmp = "|" + StringFunctions.replicate( " ", 67 - ( sTmp.length() / 2 ) ) + sTmp;
			sCab += sTmp + StringFunctions.replicate( " ", 133 - sTmp.length() ) + " |";
		}
		if ("S".equals( cbGrupo.getVlrString() ) ) {
		String sTmp = "GRUPO: " + txtDescGrup.getText().trim();
		sCab += "\n" + imp.comprimido();
		sTmp = "|" + StringFunctions.replicate( " ", 67 - ( sTmp.length() / 2 ) ) + sTmp;
		sCab += sTmp + StringFunctions.replicate( " ", 133 - sTmp.length() ) + " |";
	}
		
		imp.limpaPags();
		imp.montaCab();
		imp.setTitulo( "Relatorio de Demanda" );
		imp.addSubTitulo( "RELATORIO DE DEMANDA - PERIODO DE " + txtDataini.getVlrString() + " A " + txtDatafim.getVlrString() );

		while ( rs.next() ) {
			if ( imp.pRow() >= ( linPag - 1 ) ) {
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
				imp.incPags();
				imp.eject();
			}
			else if ( sDivGrupo.equals( "S" ) ) {
				if ( ( sCodgrup.length() > 0 ) && ( !sCodgrup.equals( rs.getString( "Codgrup" ) ) ) ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.incPags();
					imp.eject();
				}
			}

			sCodgrup = rs.getString( "codgrup" );

			if ( imp.pRow() == 0 ) {
				imp.impCab( 136, true );

				imp.say( imp.pRow() + 0, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 0, sOrdenado );

				if ( sCab.length() > 0 )
					imp.say( imp.pRow() + 0, 0, sCab );
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );

				if ( sDivGrupo.equals( "S" ) ) {
					String sDescGrup = rs.getString( "DescGrup" );
					sDescGrup = sDescGrup != null ? sDescGrup.trim() : "";
					sDescGrup = "|" + StringFunctions.replicate( " ", 67 - ( sDescGrup.length() / 2 ) ) + sDescGrup;
					sDescGrup += StringFunctions.replicate( " ", 133 - sDescGrup.length() ) + " |";
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, sDescGrup );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
				}

				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 0, "|Cód." );
				imp.say( imp.pRow() + 0, 17, "|Produto" );
				imp.say( imp.pRow() + 0, 64, "| S. ini." );
				imp.say( imp.pRow() + 0, 73, "| Comp." );
				imp.say( imp.pRow() + 0, 82, "| Dev.c." );
				imp.say( imp.pRow() + 0, 91, "| O.ent." );
				imp.say( imp.pRow() + 0, 100, "| Vend." );
				imp.say( imp.pRow() + 0, 109, "| Dev.f." );
				imp.say( imp.pRow() + 0, 118, "| O.sai." );
				imp.say( imp.pRow() + 0, 127, "| Saldo" );
				imp.say( imp.pRow() + 0, 135, "|" );
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );

			}

			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 0, "| " + Funcoes.copy( rs.getString( sCampo ), 0, 13 ) );
			imp.say( imp.pRow() + 0, 17, "| " + Funcoes.copy( rs.getString( "DescProd" ), 0, 40 ) );
			imp.say( imp.pRow() + 0, 62, "| " + Funcoes.alinhaDir( rs.getInt( "SLDINI" ), 7 ) );
			imp.say( imp.pRow() + 0, 71, "| " + Funcoes.alinhaDir( rs.getInt( "VLRCOMPRAS" ), 7 ) );
			imp.say( imp.pRow() + 0, 80, "| " + Funcoes.alinhaDir( rs.getInt( "VLRDEVENT" ), 7 ) );
			imp.say( imp.pRow() + 0, 89, "| " + Funcoes.alinhaDir( rs.getInt( "VLROUTENT" ), 7 ) );
			imp.say( imp.pRow() + 0, 98, "| " + Funcoes.alinhaDir( rs.getInt( "VLRVENDAS" ), 7 ) );
			imp.say( imp.pRow() + 0, 107, "| " + Funcoes.alinhaDir( rs.getInt( "VLRDEVSAI" ), 7 ) );
			imp.say( imp.pRow() + 0, 116, "| " + Funcoes.alinhaDir( rs.getInt( "VLROUTSAI" ), 7 ) );
			imp.say( imp.pRow() + 0, 125, "| " + Funcoes.alinhaDir( rs.getInt( "SLDFIM" ), 7 ) );
			imp.say( imp.pRow() + 0, 135, "|" );
		}

		imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
		imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
		imp.eject();
		imp.fechaGravacao();

		con.commit();
		
		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
		
	} catch ( SQLException err ) {
		Funcoes.mensagemErro( this, "Erro consulta de estoque!\n" + err.getMessage(), true, con, err );
		err.printStackTrace();
	}


}
		
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcGrup.setConexao( cn );
		lcMarca.setConexao( cn );
	}

}
