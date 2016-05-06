/**
 * @version 19/06/2004 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRConfEstoq.java <BR>
 * 
 *                      Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                      Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRConfEstoq extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private Vector<String> vLabTipoRel = new Vector<String>();

	private Vector<String> vValTipoRel = new Vector<String>();

	private JRadioGroup<?, ?> rgTipoRel = null;
	
	private Vector<String> vLabsGraf = new Vector<String>( 2 );
	
	private Vector<String> vValsGraf = new Vector<String>( 2 ) ;
	
	private JRadioGroup<?, ?> rgTipo = null;

	private JCheckBoxPad cbTipoMovEstoq = null;

	private JCheckBoxPad cbAtivo = null;

	public FRConfEstoq() {

		setTitulo( "Relatório de Conferência de Estoque" );
		setAtribos( 80, 30, 350, 250 );

		vLabTipoRel.addElement( "Saldos de lotes" );
		vLabTipoRel.addElement( "Saldos de produtos" );
		vValTipoRel.addElement( "L" );
		vValTipoRel.addElement( "S" );
		
		vLabsGraf.addElement( "Gráfico" );
		vLabsGraf.addElement( "Texto" );
		vValsGraf.addElement( "G" );
		vValsGraf.addElement( "T" );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabsGraf, vValsGraf );
		rgTipo.setVlrString( "T" );

		rgTipoRel = new JRadioGroup<String, String>( 1, 3, vLabTipoRel, vValTipoRel );

		cbTipoMovEstoq = new JCheckBoxPad( "Apenas tipos de movimento c/ contr. de estoq.", "S", "N" );
		cbTipoMovEstoq.setVlrString( "S" );

		cbAtivo = new JCheckBoxPad( "Somente ativos.", "S", "N" );
		cbAtivo.setVlrString( "N" );

		adic( new JLabelPad( "Conferência" ), 7, 0, 250, 20 );
		adic( rgTipoRel, 7, 20, 300, 30 );
		adic( cbTipoMovEstoq, 7, 50, 300, 30 );
		adic( cbAtivo, 7, 80, 300, 30 );
		adic (rgTipo, 7, 110, 300, 30);

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		String sTipoMovEst = cbTipoMovEstoq.getVlrString();
		StringBuilder sWhere = new StringBuilder();
	
		
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

		try {

			if ( sTipoMovEst.equals( "S" ) ) {
				sWhere.append( " AND TM.ESTOQTIPOMOV='S' " );
			}
			if ( rgTipoRel.getVlrString().equals( "L" ) ) {
				impLote( bVisualizar, sWhere, "G".equals(rgTipo.getVlrString()) );
			}

			else if ( rgTipoRel.getVlrString().equals( "S" ) ) {
				impProduto( bVisualizar, sWhere, "G".equals(rgTipo.getVlrString()), fotoemp );
			}
		} finally {

			sTipoMovEst = null;
			sWhere = null;
		}
	}

	private void impProduto( TYPE_PRINT bVisualizar, StringBuilder sWhere , boolean postscript, Blob fotoemp ) {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		BigDecimal beSldCalc = new BigDecimal( 0 );
		BigDecimal beQtdDif = new BigDecimal( 0 );
		try {
			sql.append( "SELECT DESCPROD, CODPROD, REFPROD, SLDLIQPROD, QTDINVP, QTDITCOMPRA, ");
			sql.append( "QTDFINALPRODOP, QTDEXPITRMA, QTDITVENDA, SLDMOVPROD, SLDLIQPRODAX , ");
			sql.append( "(QTDINVP + QTDITCOMPRA + QTDFINALPRODOP - QTDEXPITRMA - QTDITVENDA) SLDCA ");
			sql.append( "FROM EQCONFESTOQVW01 " + "WHERE CODEMP=? AND CODFILIAL=? AND " );
			sql.append(  cbAtivo.getVlrString().equals( "S" ) ? "ATIVOPROD='S' AND " : "" );
			sql.append( "( ( ( QTDINVP + QTDITCOMPRA + QTDFINALPRODOP - QTDEXPITRMA - QTDITVENDA) <> SLDLIQPROD ) OR " );
			sql.append( "( (QTDINVP + QTDITCOMPRA + QTDFINALPRODOP - QTDEXPITRMA - QTDITVENDA) <> SLDMOVPROD) OR " );
			sql.append( "( (QTDINVP + QTDITCOMPRA + QTDFINALPRODOP - QTDEXPITRMA - QTDITVENDA) <> SLDLIQPRODAX) OR " );
			sql.append(  "(SLDLIQPROD<>SLDMOVPROD) OR (SLDLIQPRODAX<>SLDMOVPROD) )" );
	
			System.out.println( sql.toString() );
			
			try{
				ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
				rs = ps.executeQuery();

				
				if (postscript) {
					impProdGrafico( bVisualizar, rs, fotoemp );
				} else {
					impProdTexto( bVisualizar, rs, beSldCalc, beQtdDif );
					rs.close();
					ps.close();
					con.commit();			
				}
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro executando a consulta.\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}	        
			

			
		} finally {
                sql = null;
                ps = null;
                rs = null;
                beSldCalc = null;
                beQtdDif = null;
        }
	}
	
	private void impProdGrafico( TYPE_PRINT bVisualizar, ResultSet rs, Blob fotoemp ) {
		
		String report = "relatorios/FRConferenciaEstoque.jasper";
		String label = "Conferência de Estoque";
		String sCab = "Relatorio de Conferência de Estoque por Produto";
		
	    HashMap<String, Object> hParam = new HashMap<String, Object>();
	
	    try {
			hParam.put( "LOGOEMP",  new ImageIcon(fotoemp.getBytes(1, ( int ) fotoemp.length())).getImage() );
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro carregando logotipo !\n" + e.getMessage()  );
			e.printStackTrace();
		}
	
		FPrinterJob dlGr = new FPrinterJob( report, label, sCab,  rs, hParam , this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		} else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de Conferência de estoque!" + err.getMessage(), true, con, err );
			}
		}
	
	}

	
	private void impProdTexto(TYPE_PRINT bVisualizar, ResultSet rs, BigDecimal beSldCalc, 	BigDecimal beQtdDif){
		ImprimeOS imp = null;
		int linPag = 0;

		
		imp = new ImprimeOS( "", con );
		linPag = imp.verifLinPag() - 1;
		imp.setTitulo( "Relatorio de Conferência de Estoque por Produto" );
		
		try {
			
			imp.limpaPags();

			while ( rs.next() ) {
				if ( imp.pRow() >= ( linPag - 1 ) ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.incPags();
					imp.eject();

				}
				if ( imp.pRow() == 0 ) {
					imp.montaCab();
					imp.setTitulo( "Relatorio de Comferencia de Estoque" );
					imp.addSubTitulo( "CONFERENCIA DE ESTOQUE CONSIDERANDO SALDOS POR PRODUTO" );
					imp.impCab( 136, true );

					imp.say( imp.pRow() + 0, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "| DESCRICAO DO PRODUTO" );
					imp.say( imp.pRow() + 0, 32, "| CODIGO" );
					imp.say( imp.pRow() + 0, 44, "| SALDO " );
					imp.say( imp.pRow() + 0, 54, "| QTD.IV." );
					imp.say( imp.pRow() + 0, 64, "| QTD.OP." );
					imp.say( imp.pRow() + 0, 74, "| QTD.CP." );
					imp.say( imp.pRow() + 0, 84, "| QTD.RM." );
					imp.say( imp.pRow() + 0, 94, "| QTD.VD." );
					imp.say( imp.pRow() + 0, 104, "| SLD.CA." );
					imp.say( imp.pRow() + 0, 114, "| SLD.MP." );
					imp.say( imp.pRow() + 0, 124, "| DIF.SD." );
					imp.say( imp.pRow() + 0, 135, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );

				}
				beSldCalc = rs.getBigDecimal( "QTDINVP" ).add( rs.getBigDecimal( "QTDITCOMPRA" ) );
				beSldCalc = beSldCalc.add( rs.getBigDecimal( "QTDFINALPRODOP" ) );
				beSldCalc = beSldCalc.subtract( rs.getBigDecimal( "QTDEXPITRMA" ) );
				beSldCalc = beSldCalc.subtract( rs.getBigDecimal( "QTDITVENDA" ) );

				beQtdDif = beSldCalc.subtract( rs.getBigDecimal( "SLDLIQPROD" ) );

				if ( beQtdDif.doubleValue() == 0 ) {
					beQtdDif = rs.getBigDecimal( "SLDMOVPROD" ).subtract( rs.getBigDecimal( "SLDLIQPROD" ) );
				}
	

				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 0, "|" + Funcoes.adicionaEspacos( rs.getString( "DESCPROD" ), 30 ) );
				imp.say( imp.pRow() + 0, 32, "|" + Funcoes.adicionaEspacos( rs.getString( "CODPROD" ), 10 ) );
				imp.say( imp.pRow() + 0, 44, "|" + Funcoes.alinhaDir( Funcoes.bdToStr( rs.getBigDecimal( "SLDLIQPROD" ) ).toString(), 8 ) );
				imp.say( imp.pRow() + 0, 54, "|" + Funcoes.alinhaDir( Funcoes.bdToStr( rs.getBigDecimal( "QTDINVP" ) ).toString(), 8 ) );
				imp.say( imp.pRow() + 0, 64, "|" + Funcoes.alinhaDir( Funcoes.bdToStr( rs.getBigDecimal( "QTDFINALPRODOP" ) ).toString(), 8 ) );
				imp.say( imp.pRow() + 0, 74, "|" + Funcoes.alinhaDir( Funcoes.bdToStr( rs.getBigDecimal( "QTDITCOMPRA" ) ).toString(), 8 ) );
				imp.say( imp.pRow() + 0, 84, "|" + Funcoes.adicEspacosEsquerda( rs.getDouble( "QTDEXPITRMA" ) + "", 8 ) );
				imp.say( imp.pRow() + 0, 94, "|" + Funcoes.alinhaDir( Funcoes.bdToStr( rs.getBigDecimal( "QTDITVENDA" ) ).toString(), 8 ) );
				imp.say( imp.pRow() + 0, 104, "|" + Funcoes.alinhaDir( Funcoes.bdToStr( beSldCalc ).toString(), 8 ) );
				imp.say( imp.pRow() + 0, 114, "|" + Funcoes.alinhaDir( Funcoes.bdToStr( rs.getBigDecimal( "SLDMOVPROD" ) ).toString(), 8 ) );
				imp.say( imp.pRow() + 0, 124, "|" + Funcoes.alinhaDir( Funcoes.bdToStr( beQtdDif ).toString(), 8 ) );
				imp.say( imp.pRow() + 0, 135, "|" );

			}
						// Fim da impressão do total por setor

		imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
		imp.say( imp.pRow() + 0, 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );

		imp.eject();
		imp.fechaGravacao();

	} catch ( SQLException err ) {
		Funcoes.mensagemErro( this, "Erro executando a consulta.\n" + err.getMessage(), true, con, err );
		err.printStackTrace();
	}
	if ( bVisualizar==TYPE_PRINT.VIEW ) {
		imp.preview( this );
	}
	else {
		imp.print();
	}

	}	
	private void impLote( TYPE_PRINT bVisualizar, StringBuilder sWhere, boolean postscript ) {
		
		if (postscript) {
			Funcoes.mensagemInforma( this, "Relatório gráfico não disponível para conferência de Lote!" );
			return;
		}

		String sSql = "";
		ImprimeOS imp = null;
		int linPag = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		double deSldCalc = 0;
		double deQtdDif = 0;

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.setTitulo( "Relatorio de Conferência de Estoque por Lote" );

			sSql = "SELECT P.DESCPROD,P.CODPROD,L.CODLOTE,L.SLDLIQLOTE," + "(SELECT SUM(QTDINVP) FROM EQINVPROD IT WHERE IT.CODEMPPD=P.CODEMP AND " + "IT.CODFILIALPD=P.CODFILIAL AND IT.CODPROD=P.CODPROD AND IT.CODEMPLE=L.CODEMP AND "
					+ "IT.CODFILIALLE=L.CODFILIAL AND IT.CODLOTE=L.CODLOTE) QTDINVP, " + "(SELECT SUM(QTDITCOMPRA) FROM CPITCOMPRA IC, CPCOMPRA C, EQTIPOMOV TM " + " WHERE IC.CODEMPPD=P.CODEMP AND IC.CODFILIALPD=P.CODFILIAL AND IC.CODPROD=P.CODPROD AND "
					+ " IC.CODEMPLE=L.CODEMP AND IC.CODFILIALLE=L.CODFILIAL AND IC.CODLOTE=L.CODLOTE AND " + " C.CODCOMPRA=IC.CODCOMPRA AND C.CODEMP=IC.CODEMP AND C.CODFILIAL=IC.CODFILIAL AND " + " TM.CODTIPOMOV=C.CODTIPOMOV AND TM.CODEMP=C.CODEMPTM AND TM.CODFILIAL=C.CODFILIALTM "
					+ sWhere
					+ ") QTDITCOMPRA, "
					+ "(SELECT SUM(QTDFINALPRODOP) FROM PPOP O, EQTIPOMOV TM "
					+ " WHERE O.CODEMPPD=P.CODEMP AND O.CODFILIALPD=P.CODFILIAL AND O.CODPROD=P.CODPROD AND "
					+ " O.CODEMPLE=L.CODEMP AND O.CODFILIALLE=L.CODFILIAL AND O.CODLOTE=L.CODLOTE AND "
					+ " TM.CODTIPOMOV=O.CODTIPOMOV AND TM.CODEMP=O.CODEMPTM AND TM.CODFILIAL=O.CODFILIALTM "
					+ sWhere
					+ ") QTDFINALPRODOP, "
					+ "(SELECT SUM(QTDEXPITRMA) FROM EQRMA R, EQITRMA IR, EQTIPOMOV TM "
					+ " WHERE IR.CODEMPPD=P.CODEMP AND IR.CODFILIALPD=P.CODFILIAL AND IR.CODPROD=P.CODPROD AND "
					+ " R.CODRMA=IR.CODRMA AND R.CODEMP=IR.CODEMP AND R.CODFILIAL=IR.CODFILIAL AND "
					+ " IR.CODEMPLE=L.CODEMP AND IR.CODFILIALLE=L.CODFILIAL AND IR.CODLOTE=L.CODLOTE AND "
					+ " TM.CODTIPOMOV=R.CODTIPOMOV AND TM.CODEMP=R.CODEMPTM AND TM.CODFILIAL=R.CODFILIALTM "
					+ sWhere
					+ ") QTDEXPITRMA, "
					+ "(SELECT SUM(QTDITVENDA) FROM VDITVENDA IV, VDVENDA V, EQTIPOMOV TM "
					+ " WHERE IV.CODEMPPD=P.CODEMP AND IV.CODFILIALPD=P.CODFILIAL AND IV.CODPROD=P.CODPROD AND "
					+ " IV.CODEMPLE=L.CODEMP AND IV.CODFILIALLE=L.CODFILIAL AND IV.CODLOTE=L.CODLOTE AND "
					+ " V.CODVENDA=IV.CODVENDA AND V.TIPOVENDA=IV.TIPOVENDA AND V.CODEMP=IV.CODEMP AND "
					+ " V.CODFILIAL=IV.CODFILIAL AND (NOT SUBSTR(V.STATUSVENDA,1,1)='C') AND "
					+ " TM.CODTIPOMOV=V.CODTIPOMOV AND TM.CODEMP=V.CODEMPTM AND "
					+ " TM.CODFILIAL=V.CODFILIALTM "
					+ sWhere
					+ ") QTDITVENDA "
					+ "FROM EQPRODUTO P,EQLOTE L WHERE P.cloteprod='S' "
					+ "AND L.CODEMP=P.CODEMP AND L.CODFILIAL=P.CODFILIAL AND L.CODPROD=P.CODPROD "
					+ "AND P.CODEMP=? AND P.CODFILIAL=? "
					+ ( cbAtivo.getVlrString().equals( "S" ) ? "AND P.ATIVOPROD='S'" : "" )
					+ "AND ( NOT L.SLDLIQLOTE=( "
					+ "( COALESCE( (SELECT SUM(QTDINVP) FROM EQINVPROD IT WHERE IT.CODEMPPD=P.CODEMP AND "
					+ "IT.CODFILIALPD=P.CODFILIAL AND IT.CODPROD=P.CODPROD AND IT.CODEMPLE=L.CODEMP AND "
					+ "IT.CODFILIALLE=L.CODFILIAL AND IT.CODLOTE=L.CODLOTE),0) ) + "
					+ "( COALESCE( (SELECT SUM(QTDITCOMPRA) FROM CPITCOMPRA IC, CPCOMPRA C, EQTIPOMOV TM "
					+ " WHERE IC.CODEMPPD=P.CODEMP AND IC.CODFILIALPD=P.CODFILIAL AND IC.CODPROD=P.CODPROD AND "
					+ " IC.CODEMPLE=L.CODEMP AND IC.CODFILIALLE=L.CODFILIAL AND IC.CODLOTE=L.CODLOTE AND "
					+ " C.CODCOMPRA=IC.CODCOMPRA AND C.CODEMP=IC.CODEMP AND C.CODFILIAL=IC.CODFILIAL AND "
					+ " TM.CODTIPOMOV=C.CODTIPOMOV AND TM.CODEMP=C.CODEMPTM AND TM.CODFILIAL=C.CODFILIALTM "
					+ sWhere
					+ "),0) ) + "
					+ "(COALESCE( (SELECT SUM(QTDFINALPRODOP) FROM PPOP O, EQTIPOMOV TM "
					+ " WHERE O.CODEMPPD=P.CODEMP AND O.CODFILIALPD=P.CODFILIAL AND O.CODPROD=P.CODPROD AND "
					+ " O.CODEMPLE=L.CODEMP AND O.CODFILIALLE=L.CODFILIAL AND O.CODLOTE=L.CODLOTE AND "
					+ " TM.CODTIPOMOV=O.CODTIPOMOV AND TM.CODEMP=O.CODEMPTM AND TM.CODFILIAL=O.CODFILIALTM "
					+ sWhere
					+ "),0) ) - "
					+ "(COALESCE( (SELECT SUM(QTDEXPITRMA) FROM EQRMA R, EQITRMA IR, EQTIPOMOV TM "
					+ " WHERE IR.CODEMPPD=P.CODEMP AND IR.CODFILIALPD=P.CODFILIAL AND IR.CODPROD=P.CODPROD AND "
					+ " R.CODRMA=IR.CODRMA AND R.CODEMP=IR.CODEMP AND R.CODFILIAL=IR.CODFILIAL AND "
					+ " IR.CODEMPLE=L.CODEMP AND IR.CODFILIALLE=L.CODFILIAL AND IR.CODLOTE=L.CODLOTE AND "
					+ " TM.CODTIPOMOV=R.CODTIPOMOV AND TM.CODEMP=R.CODEMPTM AND TM.CODFILIAL=R.CODFILIALTM "
					+ sWhere
					+ "),0) ) - "
					+ "( COALESCE( (SELECT SUM(QTDITVENDA) FROM VDITVENDA IV, VDVENDA V, EQTIPOMOV TM "
					+ " WHERE IV.CODEMPPD=P.CODEMP AND IV.CODFILIALPD=P.CODFILIAL AND IV.CODPROD=P.CODPROD AND "
					+ " IV.CODEMPLE=L.CODEMP AND IV.CODFILIALLE=L.CODFILIAL AND IV.CODLOTE=L.CODLOTE AND "
					+ " V.CODVENDA=IV.CODVENDA AND V.TIPOVENDA=IV.TIPOVENDA AND V.CODEMP=IV.CODEMP AND "
					+ " V.CODFILIAL=IV.CODFILIAL AND (NOT SUBSTR(V.STATUSVENDA,1,1)='C') AND "
					+ " TM.CODTIPOMOV=V.CODTIPOMOV AND TM.CODEMP=V.CODEMPTM AND "
					+ " TM.CODFILIAL=V.CODFILIALTM "
					+ sWhere
					+ "),0 ) ) "
					+ ")) ORDER BY P.DESCPROD,L.CODLOTE";
			// System.out.println(sSql);

			try {
				ps = con.prepareStatement( sSql );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );

				rs = ps.executeQuery();

				imp.limpaPags();

				while ( rs.next() ) {
					if ( imp.pRow() >= ( linPag - 1 ) ) {
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.incPags();
						imp.eject();

					}
					if ( imp.pRow() == 0 ) {
						imp.montaCab();
						imp.setTitulo( "Relatorio de Comferencia de Estoque" );
						imp.addSubTitulo( "CONFERENCIA DE ESTOQUE CONSIDERANDO SALDOS POR LOTE" );
						imp.impCab( 136, true );

						imp.say( imp.pRow() + 0, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "| DESCRICAO" );
						imp.say( imp.pRow() + 0, 35, "| CODIGO" );
						imp.say( imp.pRow() + 0, 47, "| LOTE" );
						imp.say( imp.pRow() + 0, 61, "| SALDO " );
						imp.say( imp.pRow() + 0, 70, "| QTD.IV." );
						imp.say( imp.pRow() + 0, 79, "| QTD.OP." );
						imp.say( imp.pRow() + 0, 88, "| QTD.CP." );
						imp.say( imp.pRow() + 0, 97, "| QTD.RM." );
						imp.say( imp.pRow() + 0, 106, "| QTD.VD." );
						imp.say( imp.pRow() + 0, 115, "| SLD.CA." );
						imp.say( imp.pRow() + 0, 124, "| DIF.SD." );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );

					}

					deSldCalc = rs.getDouble( "QTDINVP" ) + rs.getDouble( "QTDITCOMPRA" ) + rs.getDouble( "QTDFINALPRODOP" ) - rs.getDouble( "QTDEXPITRMA" ) - rs.getDouble( "QTDITVENDA" );
					deQtdDif = deSldCalc - rs.getDouble( "SLDLIQLOTE" );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + Funcoes.adicionaEspacos( rs.getString( "DESCPROD" ), 33 ) );
					imp.say( imp.pRow() + 0, 35, "|" + Funcoes.adicionaEspacos( rs.getString( "CODPROD" ), 10 ) );
					imp.say( imp.pRow() + 0, 47, "|" + Funcoes.adicionaEspacos( rs.getString( "CODLOTE" ), 13 ) );
					imp.say( imp.pRow() + 0, 61, "|" + Funcoes.adicEspacosEsquerda( rs.getDouble( "SLDLIQLOTE" ) + "", 8 ) );
					imp.say( imp.pRow() + 0, 70, "|" + Funcoes.adicEspacosEsquerda( rs.getDouble( "QTDINVP" ) + "", 8 ) );
					imp.say( imp.pRow() + 0, 79, "|" + Funcoes.adicEspacosEsquerda( rs.getDouble( "QTDFINALPRODOP" ) + "", 8 ) );
					imp.say( imp.pRow() + 0, 88, "|" + Funcoes.adicEspacosEsquerda( rs.getDouble( "QTDITCOMPRA" ) + "", 8 ) );
					imp.say( imp.pRow() + 0, 97, "|" + Funcoes.adicEspacosEsquerda( rs.getDouble( "QTDEXPITRMA" ) + "", 8 ) );
					imp.say( imp.pRow() + 0, 106, "|" + Funcoes.adicEspacosEsquerda( rs.getDouble( "QTDITVENDA" ) + "", 8 ) );
					imp.say( imp.pRow() + 0, 115, "|" + Funcoes.adicEspacosEsquerda( deSldCalc + "", 8 ) );
					imp.say( imp.pRow() + 0, 124, "|" + Funcoes.adicEspacosEsquerda( deQtdDif + "", 8 ) );
					imp.say( imp.pRow() + 0, 135, "|" );

				}

				rs.close();
				ps.close();
				con.commit();

				// Fim da impressão do total por setor

				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );

				imp.eject();
				imp.fechaGravacao();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro executando a consulta.\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}
			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}
		} finally {
			sSql = null;
			imp = null;
			ps = null;
			rs = null;
			deSldCalc = 0;
			deQtdDif = 0;
		}

	}

}
