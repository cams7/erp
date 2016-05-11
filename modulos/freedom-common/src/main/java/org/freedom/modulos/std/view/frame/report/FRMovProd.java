/**
 * @version 08/12/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freeedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRListaProd.java <BR>
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
 *                      Relatório de produtos.
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

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

public class FRMovProd extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDe = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtA = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCodForn = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescForn = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodGrupo = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtDescGrupo = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodAlmox = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescAlmox = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodMarca = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldFK txtDescMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtSiglaMarca = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JCheckBoxPad cbAgrupar = new JCheckBoxPad( "Agrupar por fornecedor", "S", "N" );

	private JRadioGroup<String, String> rgOrdem = null;

	private JRadioGroup<String, String> rgAtivoProd = null;

	private JRadioGroup<String, String> rgProduto = null;

	private JRadioGroup<String, String> rgTipo = null;

	private Vector<String> vLabsGraf = new Vector<String>( 2 );

	private Vector<String> vValsGraf = new Vector<String>( 2 ) ;

	private ListaCampos lcAlmox = new ListaCampos( this );

	private ListaCampos lcCodForn = new ListaCampos( this );

	private ListaCampos lcGrupo = new ListaCampos( this );

	private ListaCampos lcMarca = new ListaCampos( this );

	public static enum COL_MOVPROD {
		ORDEM, DE, A, ATIVOPROD,CODFORN,DESCFORN,CODALMOX,DESCALMOX, PRODUTO, CODMARCA, DESCMARCA
	};

	public FRMovProd() {

		setTitulo( "Relatório de Produtos" );
		setAtribos( 50, 50, 480, 550 );

		montaListaCampos();
		montaRadioGrups();
		montaTela();

		cbAgrupar.setVlrString( "N" );

		Calendar periodo = Calendar.getInstance();
		txtDatafim.setVlrDate( periodo.getTime() );
		periodo.set( Calendar.DAY_OF_MONTH, 1 );
		txtDataini.setVlrDate( periodo.getTime() );
	}

	private void montaListaCampos() {

		lcAlmox.add( new GuardaCampo( txtCodAlmox, "CodAlmox", "Cód.almox.", ListaCampos.DB_PK, false ) );
		lcAlmox.add( new GuardaCampo( txtDescAlmox, "DescAlmox", "Descrição do almoxarifado", ListaCampos.DB_SI, false ) );
		lcAlmox.montaSql( false, "ALMOX", "EQ" );
		lcAlmox.setReadOnly( true );
		txtCodAlmox.setTabelaExterna( lcAlmox, null );
		txtCodAlmox.setFK( true );
		txtCodAlmox.setNomeCampo( "CodAlmox" );

		lcCodForn.add( new GuardaCampo( txtCodForn, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcCodForn.add( new GuardaCampo( txtDescForn, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcCodForn.montaSql( false, "FORNECED", "CP" );
		lcCodForn.setReadOnly( true );
		txtCodForn.setTabelaExterna( lcCodForn, null );
		txtCodForn.setFK( true );
		txtCodForn.setNomeCampo( "CodFor" );

		lcGrupo.add( new GuardaCampo( txtCodGrupo, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrupo.add( new GuardaCampo( txtDescGrupo, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrupo.montaSql( false, "GRUPO", "EQ" );
		lcGrupo.setReadOnly( true );
		txtCodGrupo.setTabelaExterna( lcGrupo, null );
		txtCodGrupo.setFK( true );
		txtCodGrupo.setNomeCampo( "CodGrup" );

		lcMarca.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca", ListaCampos.DB_PK, false ) );
		lcMarca.add( new GuardaCampo( txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false ) );
		lcMarca.add( new GuardaCampo( txtSiglaMarca, "SiglaMarca", "Sigla", ListaCampos.DB_SI, false ) );
		txtCodMarca.setTabelaExterna( lcMarca, null );
		txtCodMarca.setNomeCampo( "CodMarca" );
		txtCodMarca.setFK( true );
		lcMarca.setReadOnly( true );
		lcMarca.montaSql( false, "MARCA", "EQ" );
	}

	private void montaRadioGrups() {

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		vLabs.addElement( "Código" );
		vLabs.addElement( "Descrição" );
		vVals.addElement( "C" );
		vVals.addElement( "D" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "D" );

		Vector<String> vLabs1 = new Vector<String>();
		Vector<String> vVals1 = new Vector<String>();

		vLabs1.addElement( "Ativos" );
		vLabs1.addElement( "Inativos" );
		vLabs1.addElement( "Todos" );
		vVals1.addElement( "A" );
		vVals1.addElement( "N" );
		vVals1.addElement( "T" );
		rgAtivoProd = new JRadioGroup<String, String>( 2, 2, vLabs1, vVals1 );
		rgAtivoProd.setVlrString( "A" );

		Vector<String> vLabs2 = new Vector<String>();
		Vector<String> vVals2 = new Vector<String>();

		vLabs2.addElement( "Comercio" );
		vLabs2.addElement( "Serviço" );
		vLabs2.addElement( "Fabricação" );
		vLabs2.addElement( "Mat.Prima" );
		vLabs2.addElement( "Patrimonio" );
		vLabs2.addElement( "Consumo" );
		vLabs2.addElement( "Todos" );
		vVals2.addElement( "P" );
		vVals2.addElement( "S" );
		vVals2.addElement( "F" );
		vVals2.addElement( "M" );
		vVals2.addElement( "O" );
		vVals2.addElement( "C" );
		vVals2.addElement( "T" );

		rgProduto = new JRadioGroup<String, String>( 4, 2, vLabs2, vVals2 );
		rgProduto.setVlrString( "P" );

		vLabsGraf.addElement( "Gráfico" );
		vLabsGraf.addElement( "Texto" );
		vValsGraf.addElement( "G" );
		vValsGraf.addElement( "T" );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabsGraf, vValsGraf );
		rgTipo.setVlrString( "T" );


	}

	private void montaTela() {

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbLinha2 = new JLabelPad();
		lbLinha2.setBorder( BorderFactory.createEtchedBorder() );

		adic( new JLabelPad( "Periodo:" ), 7, 5, 80, 20 );
		adic( lbLinha, 7, 25, 230, 35 );
		adic( new JLabelPad( "De:" ), 17, 32, 25, 20 );
		adic( txtDataini, 42, 32, 80, 20 );
		adic( new JLabelPad( "à", SwingConstants.CENTER ), 122, 32, 25, 20 );
		adic( txtDatafim, 147, 32, 80, 20 );

		adic( new JLabelPad( "Ordenar por:" ), 240, 5, 200, 20 );
		adic( rgOrdem, 240, 25, 200, 35 );
		adic( new JLabelPad( "Filtrar por:" ), 7, 60, 230, 20 );
		adic( rgAtivoProd, 7, 80, 230, 50 );
		adic( new JLabelPad( "Tipo de Produto:" ), 240, 60, 200, 20 );
		adic( rgProduto, 240, 80, 200, 160 );

		adic( new JLabelPad( "Filtro por razão:" ), 7, 130, 230, 20 );
		adic( lbLinha2, 7, 150, 230, 90 );
		adic( new JLabelPad( "de:" ), 17, 160, 30, 20 );
		adic( txtDe, 50, 160, 177, 20 );
		adic( new JLabelPad( "à:" ), 17, 185, 30, 20 );
		adic( txtA, 50, 185, 177, 20 );
		adic( cbAgrupar, 17, 205, 210, 30 );

		adic( new JLabelPad( "Cód.for." ), 7, 250, 300, 20 );
		adic( txtCodForn, 7, 270, 80, 20 );
		adic( new JLabelPad( "Descrição do fornecedor" ), 90, 250, 300, 20 );
		adic( txtDescForn, 90, 270, 350, 20 );
		adic( new JLabelPad( "Cód.almox." ), 7, 290, 250, 20 );
		adic( txtCodAlmox, 7, 310, 80, 20 );
		adic( new JLabelPad( "Descrição do Almoxarifado" ), 90, 290, 250, 20 );
		adic( txtDescAlmox, 90, 310, 350, 20 );
		adic( new JLabelPad( "Cód.grupo" ), 7, 330, 250, 20 );
		adic( txtCodGrupo, 7, 350, 80, 20 );
		adic( new JLabelPad( "Descrição do grupo" ), 90, 330, 250, 20 );
		adic( txtDescGrupo, 90, 350, 350, 20 );
		adic( new JLabelPad( "Cód.marca" ), 7, 370, 250, 20 );
		adic( txtCodMarca, 7, 390, 80, 20 );
		adic( new JLabelPad( "Descrição da Marca" ), 90, 370, 250, 20 );
		adic( txtDescMarca, 90, 390, 350, 20 );

		adic( new JLabelPad( "Relatório" ), 7, 410, 250, 20 );
		adic( rgTipo, 7, 430, 250, 30 );

	}

	public String[] getValores() {

		String[] sRetorno = new String[ 11 ];

		if ( rgOrdem.getVlrString().compareTo( "C" ) == 0 ) {
			sRetorno[ COL_MOVPROD.ORDEM.ordinal()  ] = "1";
		}
		else if ( rgOrdem.getVlrString().compareTo( "D" ) == 0 ) {
			sRetorno[ COL_MOVPROD.ORDEM.ordinal()  ] = "2";
		}

		sRetorno[ COL_MOVPROD.DE.ordinal() ] = txtDe.getText();
		sRetorno[ COL_MOVPROD.A.ordinal() ] = txtA.getText();
		sRetorno[ COL_MOVPROD.ATIVOPROD.ordinal() ] = rgAtivoProd.getVlrString();
		sRetorno[ COL_MOVPROD.CODFORN.ordinal() ] = txtCodForn.getVlrString();
		sRetorno[ COL_MOVPROD.DESCFORN.ordinal() ] = txtDescForn.getVlrString();
		sRetorno[ COL_MOVPROD.CODALMOX.ordinal() ] = txtCodAlmox.getText();
		sRetorno[ COL_MOVPROD.DESCALMOX.ordinal() ] = txtDescAlmox.getText();
		sRetorno[ COL_MOVPROD.PRODUTO.ordinal() ] = rgProduto.getVlrString();
		sRetorno[ COL_MOVPROD.CODMARCA.ordinal() ] = txtCodMarca.getVlrString();
		sRetorno[ COL_MOVPROD.DESCMARCA.ordinal() ] = txtDescMarca.getText();

		return sRetorno;
	}
	
	private void carregaFiltros( String[] valores, StringBuilder where, Vector<String> filtros ) {

		if ( valores[ COL_MOVPROD.DE.ordinal() ].trim().length() > 0 ) {
			where.append( " AND DESCPROD>='" + valores[ COL_MOVPROD.DE.ordinal() ] + "'" );
			filtros.add( "PRODUTOS MAIORES QUE " + valores[ COL_MOVPROD.DE.ordinal() ].trim() );
		}
		if ( valores[ COL_MOVPROD.A.ordinal() ].trim().length() > 0 ) {
			where.append( " AND DESCPROD <= '" + valores[ COL_MOVPROD.A.ordinal() ] + "'" );
			filtros.add( "PRODUTOS MENORES QUE " + valores[ COL_MOVPROD.A.ordinal() ].trim() );
		}
		if ( valores[ COL_MOVPROD.ATIVOPROD.ordinal() ].equals( "A" ) ) {
			where.append( " AND ATIVOPROD='S'" );
			filtros.add( "PRODUTOS ATIVOS" );
		}
		else if ( valores[ COL_MOVPROD.ATIVOPROD.ordinal() ].equals( "N" ) ) {
			where.append( " AND ATIVOPROD='N'" );
			filtros.add( "PRODUTOS INATIVOS" );
		}
		else if ( valores[ COL_MOVPROD.ATIVOPROD.ordinal() ].equals( "T" ) ) {
			filtros.add( "PRODUTOS ATIVOS E INATIVOS" );
		}
		if ( valores[ COL_MOVPROD.DESCALMOX.ordinal() ].length() > 0 ) {
			where.append( " AND CODALMOX = " + valores[ COL_MOVPROD.CODALMOX.ordinal() ] );
			filtros.add( "ALMOXARIFADO = " + valores[ COL_MOVPROD.DESCALMOX.ordinal()] );
		}
		if ( valores[ COL_MOVPROD.CODMARCA.ordinal() ].length() > 0 ) {
			where.append( " AND CODMARCA = '" + valores[ COL_MOVPROD.CODMARCA.ordinal() ] + "'" );
			filtros.add( "MARCA: " + valores[ COL_MOVPROD.DESCMARCA.ordinal() ] );
		}
		if ( valores[ COL_MOVPROD.PRODUTO.ordinal() ].equals( "P" ) ) {
			where.append( " AND TIPOPROD='P'" );
			filtros.add( "TIPO PRODUTOS" );
		}
		else if ( valores[ COL_MOVPROD.PRODUTO.ordinal() ].equals( "S" ) ) {
			where.append( " AND TIPOPROD='S'" );
			filtros.add( "TIPO SERVIÇOS" );
		}
		else if ( valores[ COL_MOVPROD.PRODUTO.ordinal() ].equals( "F" ) ) {
			where.append( " AND TIPOPROD='F'" );
			filtros.add( "FABRICAÇÃO" );
		}
		else if ( valores[ COL_MOVPROD.PRODUTO.ordinal() ].equals( "M" ) ) {
			where.append( " AND TIPOPROD='M'" );
			filtros.add( "MATERIA PRIMA" );
		}
		else if ( valores[ COL_MOVPROD.PRODUTO.ordinal() ].equals( "O" ) ) {
			where.append( " AND TIPOPROD='O'" );
			filtros.add( "PATRIMONIO" );
		}
		else if ( valores[ COL_MOVPROD.PRODUTO.ordinal() ].equals( "C" ) ) {
			where.append( " AND TIPOPROD='C'" );
			filtros.add( "CONSUMO" );
		}
		else if ( valores[ COL_MOVPROD.PRODUTO.ordinal() ].equals( "T" ) ) {
			filtros.add( "TODOS OS TIPOS DE PRODUTOS" );
		}
		if ( cbAgrupar.getVlrString().equals( "N" ) ) {
			if ( !txtCodForn.getVlrString().equals( "" ) ) {
				filtros.add( "Fornecedor: " + txtDescForn.getVlrString() );
			}
		}
		if ( ! ( txtCodGrupo.getVlrString().equals( "" ) ) ) {
			filtros.add( "PRODUTOS DO GRUPO " + txtDescGrupo.getVlrString() );
			where.append( " AND PD.CODGRUP LIKE '" + txtCodGrupo.getVlrString() + "%'" );
		}
		
	}
	

	public void imprimir( TYPE_PRINT bVisualizar ) {

		Blob fotoemp = null;
		
		if ( txtDataini.getVlrString().length() < 10 || txtDatafim.getVlrString().length() < 10 ) {

			Funcoes.mensagemInforma( this, "Período inválido!" );
			return;
		}
		
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
		/*
		if ( cbAgrupar.getVlrString().equals( "S" )  )  {
			impMovProd( bVisualizar, "G".equals(rgTipo.getVlrString() ), fotoemp );
		}
		*/
		impMovProd( bVisualizar, "G".equals(rgTipo.getVlrString() ), fotoemp );
			

		
	}

	private void impMovProd( TYPE_PRINT bVisualizar, boolean postscript, Blob fotoemp ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		String[] valores = getValores();
		String sCab = "";
		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		Vector<String> filtros = new Vector<String>();
		
		carregaFiltros( valores, where, filtros );
		
		if ( cbAgrupar.getVlrString().equals( "S" ) ) {// Para agrupamento por fornecedores
			sql.append( "SELECT PD.CODPROD,PD.DESCPROD,PD.CODBARPROD,PD.CODFABPROD,PD.CODUNID,PD.TIPOPROD,PD.CODGRUP,'N' MOV,PF.CODFOR, " );
			sql.append( "(SELECT F.RAZFOR FROM CPFORNECED F WHERE F.CODFOR=PF.CODFOR AND F.CODEMP=PF.CODEMP AND F.CODFILIAL=PF.CODFILIAL) RAZFOR,PF.REFPRODFOR " );
			sql.append( "FROM EQPRODUTO PD LEFT OUTER JOIN CPPRODFOR PF ON (PD.CODPROD = PF.CODPROD AND PD.CODEMP = PF.CODEMP) " );
			sql.append( "WHERE PD.CODEMP=? AND PD.CODFILIAL=? " );
			sql.append( "AND NOT EXISTS(SELECT * FROM EQMOVPROD MV WHERE MV.CODEMPPD=PD.CODEMP " );
			sql.append( "AND MV.CODFILIALPD=PD.CODFILIAL AND MV.CODPROD=PD.CODPROD) " );
			sql.append( !valores[ COL_MOVPROD.CODFORN.ordinal() ].equals( "" ) ? "AND EXISTS(SELECT * FROM CPPRODFOR PF WHERE PF.CODPROD = PD.CODPROD AND PF.CODEMP = PD.CODEMP AND PF.CODFOR=" + valores[ COL_MOVPROD.CODFORN.ordinal() ] + " )" : "" );
			sql.append( where );
			sql.append( " UNION ALL " );
			sql.append( "SELECT PD.CODPROD,PD.DESCPROD,PD.CODBARPROD,PD.CODFABPROD,PD.CODUNID,PD.TIPOPROD,PD.CODGRUP,'S' MOV,PF.CODFOR, " );
			sql.append( "(SELECT F.RAZFOR FROM CPFORNECED F WHERE F.CODFOR=PF.CODFOR AND F.CODEMP=PF.CODEMP AND F.CODFILIAL=PF.CODFILIAL),PF.REFPRODFOR " );
			sql.append( "FROM EQPRODUTO PD LEFT OUTER JOIN CPPRODFOR PF ON (PD.CODPROD = PF.CODPROD AND PD.CODEMP = PF.CODEMP) " + "WHERE PD.CODEMP=? AND PD.CODFILIAL=? " );
			sql.append( "AND EXISTS(SELECT * FROM EQMOVPROD MV WHERE MV.CODEMPPD=PD.CODEMP " );
			sql.append( "AND MV.CODFILIALPD=PD.CODFILIAL AND MV.CODPROD=PD.CODPROD) " );
			sql.append( !valores[ COL_MOVPROD.CODFORN.ordinal() ].equals( "" ) ? "AND EXISTS(SELECT * FROM CPPRODFOR PF WHERE PF.CODPROD = PD.CODPROD AND PF.CODEMP = PD.CODEMP AND PF.CODFOR=" + valores[ COL_MOVPROD.CODFORN.ordinal() ] + " )" : "" );
			sql.append( where );
			sql.append( " ORDER BY 9," );
			sql.append( valores[ COL_MOVPROD.ORDEM.ordinal() ] );
		}
		else {
			sql.append( "SELECT PD.CODPROD,PD.DESCPROD,PD.CODBARPROD,PD.CODFABPROD,PD.CODUNID,PD.TIPOPROD,PD.CODGRUP,'N' MOV,'' CODFOR " );
			sql.append( "FROM EQPRODUTO PD " );
			sql.append( "WHERE PD.CODEMP=? AND PD.CODFILIAL=? " );
			sql.append( "AND NOT EXISTS(SELECT * FROM EQMOVPROD MV WHERE MV.CODEMPPD=PD.CODEMP " );
			sql.append( "AND MV.CODFILIALPD=PD.CODFILIAL AND MV.CODPROD=PD.CODPROD) " );
			sql.append( !valores[ COL_MOVPROD.CODFORN.ordinal() ].equals( "" ) ? "AND EXISTS(SELECT * FROM CPPRODFOR PF WHERE PF.CODPROD = PD.CODPROD AND PF.CODEMP = PD.CODEMP AND PF.CODFOR=" + valores[ COL_MOVPROD.CODFORN.ordinal() ] + " )" : "" );
			sql.append( where );
			sql.append( " UNION ALL " );
			sql.append( "SELECT PD.CODPROD,PD.DESCPROD,PD.CODBARPROD,PD.CODFABPROD,PD.CODUNID,PD.TIPOPROD,PD.CODGRUP,'S' MOV,'' CODFOR " );
			sql.append( "FROM EQPRODUTO PD " );
			sql.append( "WHERE PD.CODEMP=? AND PD.CODFILIAL=? " );
			sql.append( "AND EXISTS(SELECT * FROM EQMOVPROD MV WHERE MV.CODEMPPD=PD.CODEMP " );
			sql.append( "AND MV.CODFILIALPD=PD.CODFILIAL AND MV.CODPROD=PD.CODPROD) " );
			sql.append( !valores[ COL_MOVPROD.CODFORN.ordinal() ].equals( "" ) ? "AND EXISTS(SELECT * FROM CPPRODFOR PF WHERE PF.CODPROD=PD.CODPROD AND PF.CODEMP=PD.CODEMP AND PF.CODFOR=" + valores[ COL_MOVPROD.CODFORN.ordinal() ] + " )" : "" );
			sql.append( where );
			sql.append( " ORDER BY " );
			sql.append( valores[ COL_MOVPROD.ORDEM.ordinal() ] );
		}

		try {
			
			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PDPRODUTO" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "PDPRODUTO" ) );
	
			ResultSet rs = ps.executeQuery(); 
	
			if (postscript) {
				imprimiGrafico( bVisualizar, sCab, rs, fotoemp );
			} else {
				imprimiTexto( bVisualizar, rs, sCab );
				rs.close();
				ps.close();
				con.commit();			
			}
	
		} catch ( Exception err ) {
			err.printStackTrace();
		} 


	}

	private void imprimiGrafico( TYPE_PRINT bVisualizar, 
			String sCab, ResultSet rs, Blob fotoemp ) {
		
		String report = "relatorios/FRMovProd.jasper";
		String label = "Produto/Movimento";

		HashMap<String, Object> hParam = new HashMap<String, Object>();
		//hParam.put( "FILTROS", sFiltros1 + "FILTROS "+ sFiltros2 );
		try {
			hParam.put( "LOGOEMP",  new ImageIcon(fotoemp.getBytes(1, ( int ) fotoemp.length())).getImage() );
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro carregando logotipo !\n" + e.getMessage()  );
			e.printStackTrace();
		}
		if( "S".equals( cbAgrupar.getVlrString() ) ){

			report = "relatorios/FRMovProdFor.jasper";
			label = "Produto/Movimento por Grupo";

		}

		FPrinterJob dlGr = new FPrinterJob( report, label, sCab, rs, hParam , this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		} else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão do relatório Produto/Movimento!" + err.getMessage(), true, con, err );
			}
		}

	}


	private void imprimiTexto( TYPE_PRINT bVisualizar, ResultSet rs, String sCab )  {
		
		ImprimeOS imp = new ImprimeOS( "", con );
		String[] valores = getValores();
		StringBuilder where = new StringBuilder();
		Vector<String> filtros = new Vector<String>();


		String sTipo = "";
		int linPag = imp.verifLinPag() - 1;
		int iContaReg = 0;
		try {

			carregaFiltros( valores, where, filtros );

			imp.limpaPags();
			imp.setTitulo( "Relatório de Produtos" );
			imp.addSubTitulo( sCab );
			imp.montaCab();
	

			boolean bImpNulo = true;
			boolean bPulouPagina = false;
			String sCodFor = "";
			while( rs.next() ){
				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.say( imp.pRow() + 0, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 2, "|" + StringFunctions.replicate( " ", 60 ) + "Filtrado por:" + StringFunctions.replicate( " ", 60 ) + "|" );
					for ( int i = 0; i < filtros.size(); i++ ) {
						String sTmp = filtros.elementAt( i );
						sTmp = "|" + StringFunctions.replicate( " ", ( ( ( 134 - sTmp.length() ) / 2 ) - 1 ) ) + sTmp;
						sTmp += StringFunctions.replicate( " ", 134 - sTmp.length() ) + "|";
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 2, sTmp );
					}

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" );
					imp.say( imp.pRow() + 0, 3, "Código:" );
					imp.say( imp.pRow() + 0, 12, "|" );
					imp.say( imp.pRow() + 0, 13, " Cod.Barra:" );
					imp.say( imp.pRow() + 0, 27, "|" );
					imp.say( imp.pRow() + 0, 29, "Cod.Fab:" );
					imp.say( imp.pRow() + 0, 41, "|" );
					imp.say( imp.pRow() + 0, 43, "Descrição:" );
					imp.say( imp.pRow() + 0, 74, "|" );
					imp.say( imp.pRow() + 0, 76, "Unidade:" );
					imp.say( imp.pRow() + 0, 85, "|" );
					imp.say( imp.pRow() + 0, 86, " Mov." );
					imp.say( imp.pRow() + 0, 92, "|     Cod.Grupo" );
					imp.say( imp.pRow() + 0, 114, "|      Tipo" );
					imp.say( imp.pRow() + 0, 135, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );

				}
				if ( ( cbAgrupar.getVlrString().equals( "S" ) ) && ( !sCodFor.equals( rs.getString( "CODFOR" ) ) ) && bImpNulo || ( ( bPulouPagina ) && ( cbAgrupar.getVlrString().equals( "S" ) ) ) ) {
					if ( iContaReg > 0 && !bPulouPagina ) {
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					}
					if ( ! ( rs.getString( 10 ) == null ) ) {
						imp.say( imp.pRow() + 1, 0, "|" );
						imp.say( imp.pRow() + 0, 3, rs.getString( 10 ) );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					}
					else {
						imp.say( imp.pRow() + 1, 0, "|  FORNECEDOR NÃO INFORMADO" );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "|" );
						imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 133 ) + "|" );
						bImpNulo = false;
					}
					bPulouPagina = false;
				}

				imp.say( imp.pRow() + 1, 0, "" );
				imp.say( imp.pRow() + 0, 0, "|" );
				imp.say( imp.pRow() + 0, 3, rs.getString( "CODPROD" ) );
				imp.say( imp.pRow() + 0, 12, "|" );
				imp.say( imp.pRow() + 0, 13, rs.getString( "codBarProd" ) );
				imp.say( imp.pRow() + 0, 27, "|" );

				if ( cbAgrupar.getVlrString().equals( "N" ) ) {
					imp.say( imp.pRow() + 0, 29, rs.getString( "CodFabProd" ) != null ? Funcoes.copy( rs.getString( "CodFabProd" ), 12 ) : "" );
				}

				else {
					imp.say( imp.pRow() + 0, 29, rs.getString( "RefProdFor" ) != null ? Funcoes.copy( rs.getString( "RefProdFor" ), 12 ) : "" );
				}
				imp.say( imp.pRow() + 0, 41, "|" );
				imp.say( imp.pRow() + 0, 42, rs.getString( "DescProd" ) != null ? rs.getString( "Descprod" ).substring( 0, 30 ) : "" );
				imp.say( imp.pRow() + 0, 74, "|" );
				imp.say( imp.pRow() + 0, 76, rs.getString( "Codunid" ) );
				imp.say( imp.pRow() + 0, 85, "|" );
				imp.say( imp.pRow() + 0, 89, rs.getString( "Mov" ) );
				imp.say( imp.pRow() + 0, 92, "|" );
				imp.say( imp.pRow() + 0, 99, rs.getString( "codgrup" ) );
				imp.say( imp.pRow() + 0, 114, "|" );

				if ( valores[ COL_MOVPROD.PRODUTO.ordinal() ].equals( "T" ) ) {

					if ( rs.getString( "TIPOPROD" ).equals( "P" ) ) {
						sTipo = "Comercio";
						imp.say( imp.pRow() + 0, 119, sTipo );
					}
					else if ( rs.getString( "TIPOPROD" ).equals( "S" ) ) {
						sTipo = "Serviço";
						imp.say( imp.pRow() + 0, 119, sTipo );
					}
					else if ( rs.getString( "TIPOPROD" ).equals( "F" ) ) {
						sTipo = "Fabricação";
						imp.say( imp.pRow() + 0, 119, sTipo );
					}
					else if ( rs.getString( "TIPOPROD" ).equals( "M" ) ) {
						sTipo = "Mat.Prima";
						imp.say( imp.pRow() + 0, 119, sTipo );
					}
					else if ( rs.getString( "TIPOPROD" ).equals( "O" ) ) {
						sTipo = "Patrimônio";
						imp.say( imp.pRow() + 0, 119, sTipo );
					}
					else if ( rs.getString( "TIPOPROD" ).equals( "C" ) ) {
						sTipo = "Consumo";
						imp.say( imp.pRow() + 0, 119, sTipo );
					}
				}

				imp.say( imp.pRow() + 0, 135, "|" );

				sCodFor = rs.getString( "CodFor" ) == null ? "" : rs.getString( "CodFor" );

				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.incPags();
					bPulouPagina = true;
					imp.eject();
				}
				iContaReg++;
			}	
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
			imp.eject();

			imp.fechaGravacao();

			con.commit();

		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao monta relátorio!\n" + err.getMessage(), true, con, err );
		}
		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcAlmox.setConexao( cn );
		lcCodForn.setConexao( cn );
		lcGrupo.setConexao( cn );
		lcMarca.setConexao( cn );
	}
}
