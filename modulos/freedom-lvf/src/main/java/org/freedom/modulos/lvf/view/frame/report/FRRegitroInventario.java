/**
 * @version 05/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.lvf <BR>
 *         Classe:
 * @(#)FRRegistroEntrada.java <BR>
 * 
 *                            Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                            modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                            na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                            Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                            sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                            Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                            Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                            de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                            Comentario sobre a classe.
 * 
 */

package org.freedom.modulos.lvf.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRRegitroInventario extends FRelatorio {

	private static final long serialVersionUID = 1;

	private final JTextFieldPad txtDtEstoq = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtPaginaIncial = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JRadioGroup<?, ?> rgCusto = null;

	private Vector<String> vDescCusto = new Vector<String>();

	private Vector<String> vOpcCusto = new Vector<String>();

	public FRRegitroInventario() {

		super();
		setTitulo( "Registro de Entrada" );
		setAtribos( 50, 50, 320, 230 );

		montaTela();

		Calendar cal = Calendar.getInstance();
		txtDtEstoq.setVlrDate( cal.getTime() );

		txtPaginaIncial.setRequerido( true );
	}

	private void montaTela() {
		
		vDescCusto.addElement( "C.PEPS" );
		vDescCusto.addElement( "C.MPM" );
		vDescCusto.addElement( "P.BASE" );
		vOpcCusto.addElement( "P" );
		vOpcCusto.addElement( "M" );
		vOpcCusto.addElement( "B" );

		rgCusto = new JRadioGroup<String, String>( 1, 3, vDescCusto, vOpcCusto );

		JLabel bordaData = new JLabel();
		bordaData.setBorder( BorderFactory.createEtchedBorder() );
		JLabel periodo = new JLabel( "Posição do estoque em: ", SwingConstants.CENTER );
		periodo.setOpaque( true );

		adic( periodo, 20, 0, 160, 20 );
		adic( txtDtEstoq, 20, 20, 100, 20 );
		adic( bordaData, 10, 10, 260, 40 );

		adic( new JLabel( "Página Inicial" ), 10, 50, 100, 20 );
		adic( txtPaginaIncial, 10, 70, 100, 20 );
		
		adic( rgCusto, 7, 100, 250, 30 );
		txtPaginaIncial.setVlrInteger(1);
		rgCusto.setVlrString( "M" );
	}

	@ Override
	public void imprimir( TYPE_PRINT visualizar ) {
		/*
		if ( txtPaginaIncial.getVlrInteger() < 1 ) {
			Funcoes.mensagemInforma( this, "Páginal incial não informada!" );
			txtPaginaIncial.requestFocus();
			return;
		} */

		try {
			
			String[] empresa = getEmpresa();

			StringBuilder sql = new StringBuilder();
	
			sql.append( "SELECT REFPROD,DESCPROD,SLDPROD,CUSTOUNIT,CUSTOTOT ,COALESCE(CODFABPROD,0) CODFABPROD," );
			sql.append( "COALESCE(CODBARPROD,0) CODBARPROD,ATIVOPROD, CODNBM, CODUNID FROM EQRELPEPSSP(?,?,?,null,null,null,null,null,null,?,null,null,null,'N','S') " );
			sql.append( "WHERE SLDPROD!=0  AND ATIVOPROD IN ('S') ORDER BY DESCPROD" );	

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			
			int param = 1;
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtEstoq.getVlrDate() ) );
			ps.setString( param++, rgCusto.getVlrString() );
			
			ResultSet rs = ps.executeQuery();

			HashMap<String, Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "CODFILIAL", Aplicativo.iCodFilial );
			hParam.put( "FOLHA", txtPaginaIncial.getVlrInteger() );
			hParam.put( "CNPJ", empresa[ 0 ] );
			hParam.put( "INSC", empresa[ 1 ] );
			hParam.put( "PERIODO", txtDtEstoq.getVlrDate() );
			hParam.put( "REPORT_CONNECTION", con.getConnection() );

			FPrinterJob dlGr = new FPrinterJob( "relatorios/RegistroInventario.jasper", "REGISTRO DE INVENTÁRIO", null, rs, hParam, this );

			if ( visualizar == TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			}

			rs.close();
			ps.close();
			
			con.commit();
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + e.getMessage() );
			e.printStackTrace();
		}

	}

	private String[] getEmpresa() {

		String[] empresa = { "", "" };

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT CNPJFILIAL, INSCFILIAL FROM SGFILIAL WHERE CODEMP=? AND CODFILIAL=? " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGFILIAL" ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				empresa[ 0 ] = rs.getString( "CNPJFILIAL" );
				empresa[ 1 ] = rs.getString( "INSCFILIAL" );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao busca dados da filial!\n" + e.getMessage() );
			e.printStackTrace();
		}

		return empresa;
	}

}
