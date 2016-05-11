/**
 * @version 08/12/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FREstoqueMin.java <BR>
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

public class FREstoqueMin extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodMarca = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldPad txtDescMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtSiglaMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JLabelPad lbCodGrup = new JLabelPad( "Cód.grupo" );

	private JLabelPad lbDescGrup = new JLabelPad( "Descrição do grupo" );

	private JLabelPad lbCodMarca = new JLabelPad( "Cód.marca" );

	private JLabelPad lbDescMarca = new JLabelPad( "Descrição da marca" );

	private JRadioGroup<?, ?> rgOrdem = null;
	
	private JRadioGroup<?, ?> rgTipo = null;
	
	private Vector<String> vLabsGraf = new Vector<String>( 2 );
	
	private Vector<String> vValsGraf = new Vector<String>( 2 ) ;

	private Vector<String> vLabs = new Vector<String>( 2 );

	private Vector<String> vVals = new Vector<String>( 2 );

	private ListaCampos lcGrup = new ListaCampos( this );

	private ListaCampos lcMarca = new ListaCampos( this );

	private JCheckBoxPad cbGrupo = new JCheckBoxPad( "Dividir por grupo", "S", "N" );

	public FREstoqueMin() {

		setTitulo( "Relatório de Estoque Mínimo" );
		setAtribos( 80, 80, 350, 280 );
		vLabs.addElement( "Código" );
		vLabs.addElement( "Descrição" );
		vVals.addElement( "C" );
		vVals.addElement( "D" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "D" );
		
		vLabsGraf.addElement( "Gráfico" );
		vLabsGraf.addElement( "Texto" );
		vValsGraf.addElement( "G" );
		vValsGraf.addElement( "T" );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabsGraf, vValsGraf );
		rgTipo.setVlrString( "T" );
		

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, true ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrup.montaSql( false, "GRUPO", "EQ" );
		lcGrup.setReadOnly( true );
		txtCodGrup.setTabelaExterna( lcGrup, null );
		txtCodGrup.setFK( true );
		txtCodGrup.setNomeCampo( "CodGrup" );

		lcMarca.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca", ListaCampos.DB_PK, true ) );
		lcMarca.add( new GuardaCampo( txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false ) );
		lcMarca.add( new GuardaCampo( txtSiglaMarca, "SiglaMarca", "Sigla", ListaCampos.DB_SI, false ) );
		lcMarca.montaSql( false, "MARCA", "EQ" );
		lcMarca.setReadOnly( true );
		txtCodMarca.setTabelaExterna( lcMarca, null );
		txtCodMarca.setFK( true );
		txtCodMarca.setNomeCampo( "CodMarca" );

		adic( lbCodGrup, 7, 0, 250, 20 );
		adic( txtCodGrup, 7, 20, 80, 20 );
		adic( lbDescGrup, 90, 0, 250, 20 );
		adic( txtDescGrup, 90, 20, 197, 20 );
		adic( lbCodMarca, 7, 40, 250, 20 );
		adic( txtCodMarca, 7, 60, 80, 20 );
		adic( lbDescMarca, 90, 40, 250, 20 );
		adic( txtDescMarca, 90, 60, 197, 20 );
		adic( rgOrdem, 7, 90, 250, 30 );
		adic( rgTipo, 7, 120, 250, 30 );
		adic( cbGrupo, 7, 160, 250, 20 );
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
			// rs.close();
			// ps.close();
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
		
		imprimirEstoque( bVisualizar, "G".equals(rgTipo.getVlrString()) , fotoemp );
				
	}
	
	

	public void imprimirEstoque( TYPE_PRINT bVisualizar, boolean postscript, Blob fotoemp ) {

		String sOrdem = rgOrdem.getVlrString();
		String sCab = "";
		String sWhere = "";
		String sOrdenado = "";
		String sOrdemGrupo = "";
		String sDivGrupo = "";
		String sCodgrup = "";
     
		if ( sDivGrupo.equals( "S" ) ) {
			sOrdemGrupo = "P.CODGRUP,";
		}
		else {
			sOrdemGrupo = "";
		}

		if ( sOrdem.equals( "C" ) ) {
			if ( comRef() ) {
				sOrdem = sOrdemGrupo + "P.REFPROD";
				sOrdenado = "ORDENADO POR REFERENCIA";
			}
			else {
				sOrdem = sOrdemGrupo + "P.CODPROD";
				sOrdenado = "ORDENADO POR CODIGO";
			}
		}
		else {
			sOrdem = sOrdemGrupo + "P.DESCPROD";
			sOrdenado = "ORDENADO POR DESCRICAO";
		}
		sOrdenado = StringFunctions.replicate( " ", 67 - ( sOrdenado.length() / 2 ) ) + sOrdenado;
		sOrdenado += StringFunctions.replicate( " ", 132 - sOrdenado.length() );

		if ( txtCodGrup.getText().trim().length() > 0 ) {
			sWhere += " AND P.CODGRUP LIKE '" + txtCodGrup.getText().trim() + "%'";
			String sTmp = "GRUPO: " + txtDescGrup.getText().trim();
			sTmp = StringFunctions.replicate( " ", 67 - ( sTmp.length() / 2 ) ) + sTmp;
			sCab += sTmp + StringFunctions.replicate( " ", 133 - sTmp.length() ) + " |";
		}
		if ( txtCodMarca.getText().trim().length() > 0 ) {
			sWhere += " AND P.CODMARCA = '" + txtCodMarca.getText().trim() + "'";
			String sTmp = "MARCA: " + txtDescMarca.getText().trim();
			sTmp = StringFunctions.replicate( " ", 67 - ( sTmp.length() / 2 ) ) + sTmp;
			sCab += sTmp + StringFunctions.replicate( " ", 133 - sTmp.length() ) + " |";
		}
/*
		String sSQL = "SELECT P.CODGRUP,P.CODPROD,P.REFPROD,P.DESCPROD," 
			+ "P.SLDLIQPROD,P.QTDMINPROD,CAST((P.QTDMINPROD-P.SLDLIQPROD) AS DECIMAL(6,2)) QTDFALTA," 
			+ "P.CUSTOMPMPROD,CAST((P.SLDLIQPROD*P.CUSTOMPMPROD) AS DECIMAL(12,2)) VLRESTOQUE," 
			+ "CAST((P.QTDMINPROD-P.SLDLIQPROD)*P.CUSTOMPMPROD AS DECIMAL(12,2)) VLRFALTA," 
			+ "G.DESCGRUP,P.DTULTCPPROD "
			+ "FROM EQPRODUTO P,EQGRUPO G "
			+ "WHERE G.CODGRUP=P.CODGRUP AND P.SLDLIQPROD<P.QTDMINPROD " 
			+ " AND P.ATIVOPROD='S' " 
			+ sWhere 
			+ " ORDER BY " 
			+ sOrdem;
*/
		
			StringBuffer sSql = new StringBuffer();
		
			sSql.append( "SELECT P.CODGRUP,P.CODPROD,P.REFPROD,P.DESCPROD,");
			sSql.append( "cast((select custounit from eqcustoprodsp(P.CODEMP,P.codfilial,P.codprod, cast('TODAY' AS DATE),'M',null,null,null,'S')) as decimal(15,2)) SLDLIQPROD,");
			sSql.append( "P.QTDMINPROD," );
			sSql.append( "cast((select custounit from eqcustoprodsp(P.CODEMP,P.codfilial,P.codprod," );
			sSql.append( "cast('TODAY' AS DATE),'M',null,null,null,'S')) as decimal (15,2)) custompmpprod," );
			sSql.append( "G.DESCGRUP," );
			sSql.append( "(select first 1 mp.dtmovprod from eqmovprod mp where mp.tipomovprod='E' and codemppd=p.codemp and codfilialpd=p.codfilial and codprod=p.codprod " );
			sSql.append(  " and codemp=p.codemp and codfilial=p.codfilial order by mp.dtmovprod desc, mp.codmovprod desc) DTULTCPPROD " );
			sSql.append(  "FROM EQPRODUTO P,EQGRUPO G " );
			sSql.append(  "WHERE G.CODGRUP=P.CODGRUP AND P.SLDLIQPROD<P.QTDMINPROD " );
			sSql.append(  " AND P.ATIVOPROD='S' " );
			sSql.append(  sWhere ); 
			sSql.append(  " ORDER BY " ); 
			sSql.append(  sOrdem );
		
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {
			
			ps = con.prepareStatement( sSql.toString() );
			rs = ps.executeQuery();
		
			
			if (postscript) {
				impEstoqGrafico( bVisualizar, sCab, rs, fotoemp );
			} else {
				impEstoqTexto( bVisualizar, sCab, rs, sCodgrup, sOrdenado );
				rs.close();
				ps.close();
				con.commit();			
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta de Estoque Mínimo!\n" + err.getMessage(), true, con, err );
		}
		
		

	}
	private void impEstoqGrafico( TYPE_PRINT bVisualizar, 
			String sCab, ResultSet rs, Blob fotoemp ) {
		
		String report = "relatorios/EstoqueMin.jasper";
		String label = "Estoque Mínimo";
		
	    HashMap<String, Object> hParam = new HashMap<String, Object>();
		//hParam.put( "FILTROS", sFiltros1 + "FILTROS "+ sFiltros2 );
		try {
			hParam.put( "LOGOEMP",  new ImageIcon(fotoemp.getBytes(1, ( int ) fotoemp.length())).getImage() );
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro carregando logotipo !\n" + e.getMessage()  );
			e.printStackTrace();
		}
	
		FPrinterJob dlGr = new FPrinterJob( report, label, sCab, rs, hParam , this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		} else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de Estoque Mínimo!" + err.getMessage(), true, con, err );
			}
		}
	
	}

	private void impEstoqTexto( TYPE_PRINT bVisualizar, String sCab, ResultSet rs, String sCodgrup, String sOrdenado ) {
		
		String sDivGrupo = "";
		BigDecimal bSldLiqProd = new BigDecimal( "0" );
		BigDecimal bQtdMinProd = new BigDecimal( "0" );
		BigDecimal bQtdFaltaProd = new BigDecimal( "0" );
		BigDecimal bVlrFaltaProd = new BigDecimal( "0" );
		BigDecimal bVlrEstoqProd = new BigDecimal( "0" );
		sDivGrupo = cbGrupo.getVlrString();
				
		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.limpaPags();
		imp.montaCab();
		imp.setTitulo( "Relatorio de Estoque Abaixo do Minímo" );
		imp.addSubTitulo( sCab );
		imp.addSubTitulo( sOrdenado );
		
		boolean hasData = false;
		
		try { 	
			
		while ( rs.next() ) {
			
			bSldLiqProd = new BigDecimal( "0" );
			bQtdMinProd = new BigDecimal( "0" );
			bQtdFaltaProd = new BigDecimal( "0" );
			bVlrFaltaProd = new BigDecimal( "0" );
			bVlrEstoqProd = new BigDecimal( "0" );
			
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

				if ( sDivGrupo.equals( "S" ) ) {
					hasData = true;
					String sDescGrup = rs.getString( "DescGrup" );
					sDescGrup = sDescGrup != null ? sDescGrup.trim() : "";
					sDescGrup = "|" + StringFunctions.replicate( " ", 67 - ( sDescGrup.length() / 2 ) ) + sDescGrup;
					sDescGrup += StringFunctions.replicate( " ", 133 - sDescGrup.length() ) + " |";
					imp.say( imp.pRow() + 0, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, sDescGrup );
				}

				imp.say( imp.pRow() + ( hasData ? 1 : 0 ), 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 0, "| Linha.        | Ref.         |" + " Descrição                         | U.Compra  | Estoq.|" + " Min. | Falta| C.Unit. | C.Estoq.  |  C.Falta |" );
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
			}

			if ( rs.getString( "sldliqprod" ) != null ) {
				bSldLiqProd = bSldLiqProd.add( rs.getBigDecimal( "sldliqprod" ) );
			}
			if ( rs.getString( "qtdminprod" ) != null ) {
				bQtdMinProd = bQtdMinProd.add( new BigDecimal( rs.getString( "qtdminprod" ) ) );
			}
			if ( rs.getString( "sldliqprod" ) != null && rs.getString( "qtdminprod" ) != null ) {
				bQtdFaltaProd = bQtdMinProd.subtract( bSldLiqProd );
			}
			if ( rs.getString( "sldliqprod" ) != null && rs.getString( "custompmpprod" ) != null ) {
				bVlrEstoqProd = bSldLiqProd.add( rs.getBigDecimal( "custompmpprod" ) );
			}
			if ( rs.getBigDecimal( "sldliqprod" ) != null && rs.getBigDecimal( "custompmpprod" ) != null && rs.getBigDecimal( "qtdminprod" ) != null 
					&& rs.getBigDecimal( "custompmpprod" ).floatValue()>0 
			) {
				bVlrFaltaProd = (bQtdMinProd.subtract( bSldLiqProd )).multiply( rs.getBigDecimal( "custompmpprod" )  );
				if(bVlrFaltaProd.floatValue()<=0)
					bVlrFaltaProd = new BigDecimal(0);
			}

			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow(), 0, "| " + Funcoes.copy( rs.getString( "Codgrup" ), 0, 14 ) + "| " + Funcoes.copy( rs.getString( "Refprod" ), 0, 13 ) + "| " + Funcoes.copy( rs.getString( "Descprod" ), 0, 34 ) + "| "

					+ ( rs.getDate( "DtUltCpProd" ) != null ? StringFunctions.sqlDateToStrDate( rs.getDate( "DtUltCpProd" ) ) : "          " ) + "| " 
					
					+ Funcoes.strDecimalToStrCurrency( 6, 0, rs.getString( "sldliqprod" ) ) + "|"
					+ Funcoes.strDecimalToStrCurrency( 6, 0, rs.getString( "qtdminprod" ) ) + "|" 
//					+ Funcoes.strDecimalToStrCurrency( 6, 0, rs.getString( 7 X) ) + "|"
					+ Funcoes.strDecimalToStrCurrency( 6, 0, bQtdFaltaProd + "" ) + "|" 
					+ Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "custompmpprod" ) ) + "|"

					+ Funcoes.strDecimalToStrCurrency( 11, 2, bVlrEstoqProd + "" ) + "|" 
					+ Funcoes.strDecimalToStrCurrency( 10, 2, bVlrFaltaProd + "" ) + "|" );

		}

		imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
		imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
		imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
		
		imp.say( imp.pRow(), 0, "| " + Funcoes.copy( " ", 0, 14 ) + "| " + StringFunctions.replicate( " ", 13 ) + "| " + StringFunctions.replicate( " ", 34 ) + "| TOTAL     | " + Funcoes.strDecimalToStrCurrency( 6, 0, "" + bSldLiqProd ) + "|"
				
				+ Funcoes.strDecimalToStrCurrency( 6, 0, "" + bQtdMinProd ) + "|" 
				+ Funcoes.strDecimalToStrCurrency( 6, 0, "" + bQtdFaltaProd ) 
				
				+ "|" + StringFunctions.replicate( " ", 9 ) + "|" 
				
				+ Funcoes.strDecimalToStrCurrency( 10, 2, "" + bVlrEstoqProd ) + "|"
				+ Funcoes.strDecimalToStrCurrency( 10, 2, "" + bVlrFaltaProd ) + "|" );
	
		
		imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
		imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );

		imp.eject();

		imp.fechaGravacao();
		
		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
		
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta do Estoque!\n" + err.getMessage(), true, con, err );
		}


	}
	
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcGrup.setConexao( cn );
		lcMarca.setConexao( cn );
	}
}
