/**
 * @version 08/12/2000 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRVendasCli <BR>
 * 
 *                 Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                 modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                 na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                 Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                 sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                 Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                 Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                 de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                 Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import org.freedom.infra.model.jdbc.DbConnection;
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import net.sf.jasperreports.engine.JasperPrintManager;

public class FRVendasVend extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JRadioGroup<?, ?> rgTipo = null;

	private JRadioGroup<?, ?> rgFaturados = null;

	private JRadioGroup<?, ?> rgFinanceiro = null;

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private ListaCampos lcVendedor = new ListaCampos( this );
	
	private JCheckBoxPad cbPorConserto = new JCheckBoxPad( "Por item de O.S", "S", "N" );
	
	private JRadioGroup<?, ?> rgEmitidos = null;
	
	private Vector<String> vLabsEmit = new Vector<String>();

	private Vector<String> vValsEmit = new Vector<String>();


	public FRVendasVend() {

		setTitulo( "Vendas por Vendedor" );
		setAtribos( 80, 80, 310, 370 );

		GregorianCalendar cPeriodo = new GregorianCalendar();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );

		Vector<String> vLabs1 = new Vector<String>();
		Vector<String> vVals1 = new Vector<String>();

		vLabs1.addElement( "Código" );
		vLabs1.addElement( "Razão" );
		vLabs1.addElement( "Valor" );
		vVals1.addElement( "C" );
		vVals1.addElement( "R" );
		vVals1.addElement( "V" );

		Vector<String> vLabs2 = new Vector<String>();
		Vector<String> vVals2 = new Vector<String>();

		vLabs2.addElement( "Faturado" );
		vLabs2.addElement( "Não Faturado" );
		vLabs2.addElement( "Ambos" );
		vVals2.addElement( "S" );
		vVals2.addElement( "N" );
		vVals2.addElement( "A" );
		rgFaturados = new JRadioGroup<String, String>( 3, 1, vLabs2, vVals2 );
		rgFaturados.setVlrString( "S" );

		Vector<String> vLabs3 = new Vector<String>();
		Vector<String> vVals3 = new Vector<String>();

		vLabs3.addElement( "Financeiro" );
		vLabs3.addElement( "Não Finaceiro" );
		vLabs3.addElement( "Ambos" );
		vVals3.addElement( "S" );
		vVals3.addElement( "N" );
		vVals3.addElement( "A" );
		rgFinanceiro = new JRadioGroup<String, String>( 3, 1, vLabs3, vVals3 );
		rgFinanceiro.setVlrString( "S" );

		vLabsEmit.addElement( "Emitidos" );
		vLabsEmit.addElement( "Não emitidos" );
		vLabsEmit.addElement( "Ambos" );
		vValsEmit.addElement( "S" );
		vValsEmit.addElement( "N" );
		vValsEmit.addElement( "A" );
		rgEmitidos = new JRadioGroup<String, String>( 3, 1, vLabsEmit, vValsEmit );
		rgEmitidos.setVlrString( "A" );
		
		adic( new JLabelPad( "Periodo:" ), 7, 10, 120, 20 );
		adic( new JLabelPad( "De:" ), 7, 30, 30, 20 );
		adic( txtDataini, 37, 30, 90, 20 );
		adic( new JLabelPad( "Até:" ), 140, 30, 30, 20 );
		adic( txtDatafim, 175, 30, 90, 20 );

		adic( new JLabelPad( "Cód." ), 7, 50, 70, 20 );
		adic( txtCodVend, 7, 70, 70, 20 );
		txtCodVend.setRequerido( true );
		adic( new JLabelPad( "Nome do comissionado" ), 80, 50, 190, 20 );
		adic( txtNomeVend, 80, 70, 190, 20 );

		adic( rgFaturados, 7, 110, 120, 70 );
		adic( rgFinanceiro, 148, 110, 120, 70 );
		adic( rgEmitidos, 7, 190, 120, 70 );
		
		adic( cbPorConserto, 148, 190 , 120 , 20);

		lcVendedor.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVendedor.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVendedor.montaSql( false, "VENDEDOR", "VD" );
		lcVendedor.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVendedor, null );
		txtCodVend.setFK( true );
		txtCodVend.setNomeCampo( "CodVend" );

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
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return bRetorno;
	}

	
	public void imprimir( TYPE_PRINT bVisualizar ) {
		
		if("S".equals( cbPorConserto.getVlrString() )) {
			imprimirPorConserto( bVisualizar );
		}
		else {
			imprimirPorVenda( bVisualizar );
		}
		
	}
	
	public void imprimirPorConserto( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		String sOrdem = "";
		String sWhere1 = null;
		String sWhere2 = null;
		String sWhere3 = "";
		
		boolean bComRef = comRef();

		try {

			if ( rgFaturados.getVlrString().equals( "S" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
				sCab.append( "FATURADO" );
			}
			else if ( rgFaturados.getVlrString().equals( "N" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FATURADO" );
			}
			else if ( rgFaturados.getVlrString().equals( "A" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
			}

			if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "FINANCEIRO" );
			}
			else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FINANCEIRO" );
			}
			else if ( rgFinanceiro.getVlrString().equals( "A" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
			}
			
			if ( rgEmitidos.getVlrString().equals( "S" ) ) {
				sWhere3 = " AND V.STATUSVENDA IN ('V2','V3','P3') " ;
				sCab.append(" EMITIDOS " );
			}
			else if ( rgEmitidos.getVlrString().equals( "N" ) ) {
				sWhere3 = " AND V.STATUSVENDA NOT IN ('V2','V3','P3') ";
				sCab.append( "NAO EMITIDOS" );
			}


			sSQL.append( "select vd.codvend, ve.nomevend, coalesce(pd2.codprod,pd1.codprod) codprod, coalesce(pd2.refprod,pd1.refprod) refprod, ");
			sSQL.append( "coalesce(pd2.descprod, pd1.descprod) descprod, iv.qtditvenda, iv.precoitvenda, iv.vlrliqitvenda, vd.codcli, c.nomecli, vd.docvenda, vd.dtemitvenda " );
		
			sSQL.append( "from vdvendedor ve, vdcliente c, eqtipomov tm, vdvenda vd, vditvenda iv "); 
			sSQL.append( "left outer join eqproduto pd1 on ");
			sSQL.append( "pd1.codemp=iv.codemppd and pd1.codfilial=iv.codfilialpd and pd1.codprod=iv.codprod ");
			sSQL.append( "left outer join eqgrupo g1 on ");
			sSQL.append( "g1.codemp=pd1.codempgp and g1.codfilial=pd1.codfilialgp and g1.codgrup=pd1.codgrup ");
			sSQL.append( "left outer join vdvendaorc vo on ");
			sSQL.append( "vo.codemp=iv.codemp and vo.codfilial=iv.codfilial and vo.codvenda=iv.codvenda and vo.tipovenda=iv.tipovenda and vo.coditvenda=iv.coditvenda ");
			sSQL.append( "left outer join eqitrecmercitositorc iro on ");
			sSQL.append( "iro.codempoc=vo.codempor and iro.codfilialoc=vo.codfilialor and iro.codorc=vo.codorc and iro.tipoorc=vo.tipoorc and iro.coditorc=vo.coditorc ");
			sSQL.append( "left outer join eqitrecmerc ir on ");
			sSQL.append( "ir.codemp=iro.codemp and ir.codfilial=iro.codfilial and ir.ticket=iro.ticket and ir.coditrecmerc=iro.coditrecmerc ");
			sSQL.append( "left outer join eqproduto pd2 on ");
			sSQL.append( "pd2.codemp=ir.codemppd and pd2.codfilial=ir.codfilialpd and pd2.codprod=ir.codprod ");
			sSQL.append( "left outer join eqgrupo g2 on ");
			sSQL.append( "g2.codemp=pd2.codempgp and g2.codfilial=pd2.codfilialgp and g2.codgrup=pd2.codgrup " );
			
			
			sSQL.append( "where " );
			sSQL.append( "iv.codemp=vd.codemp and iv.codfilial=vd.codfilial and iv.codvenda=vd.codvenda and iv.tipovenda=vd.tipovenda and " );
			sSQL.append( "c.codemp=vd.codempcl and c.codfilial=vd.codfilialvd and c.codcli=vd.codcli and " );
			sSQL.append( "ve.codemp=vd.codempvd and ve.codfilial=vd.codfilialvd and ve.codvend=vd.codvend and " );
			sSQL.append( "tm.codemp=vd.codemptm and tm.codfilial=vd.codfilialtm and tm.codtipomov=vd.codtipomov and " );
			sSQL.append( "ve.codemp=? and ve.codfilial=? and ve.codvend=? and vd.dtemitvenda between ? and ? and " );
			sSQL.append( "not substr(vd.statusvenda,1,1)='C' " );
			
			sSQL.append( sWhere1 );
			sSQL.append( sWhere2 );
			sSQL.append( sWhere3 );
			
			sSQL.append( "order by vd.dtemitvenda " );

			System.out.println( "SQL:" + sSQL.toString() );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setInt( 3, txtCodVend.getVlrInteger() );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 5, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			rs = ps.executeQuery();

			imprimirGrafico( bVisualizar, rs, sCab.toString(), bComRef?"S":"N" );

			con.commit();

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatório de vendas!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sCab = null;
			sOrdem = null;
			sWhere1 = null;
			sWhere2 = null;
			sSQL = null;
			System.gc();
		}
	}
	
	public void imprimirPorVenda( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		String sOrdem = "";
		String sWhere1 = null;
		String sWhere2 = null;
		String sWhere3 = null;

		try {
			
			boolean bComRef = comRef();

			if ( rgFaturados.getVlrString().equals( "S" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
				sCab.append( "FATURADO" );
			}
			else if ( rgFaturados.getVlrString().equals( "N" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FATURADO" );
			}
			else if ( rgFaturados.getVlrString().equals( "A" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
			}

			if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "FINANCEIRO" );
			}
			else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FINANCEIRO" );
			}
			else if ( rgFinanceiro.getVlrString().equals( "A" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
			}
			
			else if ( rgEmitidos.getVlrString().equals( "N" ) ) {
				sWhere3 = " AND V.STATUSVENDA NOT IN ('V2','V3','P3') ";
				sCab.append( "NAO EMITIDOS" );
			}

			sSQL.append( "select vd.codvend, ve.nomevend, it.codprod, pd.refprod, pd.descprod, it.qtditvenda, it.precoitvenda, it.vlrliqitvenda, vd.codcli, cl.nomecli, vd.docvenda, vd.dtemitvenda " );
			sSQL.append( "from vditvenda it, eqproduto pd, vdvenda vd, vdcliente cl, vdvendedor ve, eqtipomov tm " );
			sSQL.append( "where " );
			sSQL.append( "it.codemp=vd.codemp and it.codfilial=vd.codfilial and it.codvenda=vd.codvenda and it.tipovenda=vd.tipovenda and " );
			sSQL.append( "pd.codemp=it.codemppd and pd.codfilial=it.codfilialpd and pd.codprod=it.codprod and " );
			sSQL.append( "cl.codemp=vd.codempcl and cl.codfilial=vd.codfilialvd and cl.codcli=vd.codcli and " );
			sSQL.append( "ve.codemp=vd.codempvd and ve.codfilial=vd.codfilialvd and ve.codvend=vd.codvend and " );
			sSQL.append( "tm.codemp=vd.codemptm and tm.codfilial=vd.codfilialtm and tm.codtipomov=vd.codtipomov and " );
			sSQL.append( "ve.codemp=? and ve.codfilial=? and ve.codvend=? and vd.dtemitvenda between ? and ? and " );
			sSQL.append( "not substr(vd.statusvenda,1,1)='C' " );
			sSQL.append( sWhere1 );
			sSQL.append( sWhere2 );
			sSQL.append( "order by vd.dtemitvenda " );

			System.out.println( "SQL:" + sSQL.toString() );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setInt( 3, txtCodVend.getVlrInteger() );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 5, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			rs = ps.executeQuery();

			imprimirGrafico( bVisualizar, rs, sCab.toString(), bComRef?"S":"N" );

			con.commit();

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatório de vendas!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sCab = null;
			sOrdem = null;
			sWhere1 = null;
			sWhere2 = null;
			sSQL = null;
			System.gc();
		}
	}

	public void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab, String comref ) {

		HashMap<String, Object> hparam = new HashMap<String, Object>();
		hparam.put( "COMREF", comref );
		
		FPrinterJob dlGr = new FPrinterJob( "layout/rel/REL_VENDAS_VEND_01.jasper", "Vendas por Vendedor", sCab, rs, hparam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de vendas por cliente!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVendedor.setConexao( cn );
	}
}
