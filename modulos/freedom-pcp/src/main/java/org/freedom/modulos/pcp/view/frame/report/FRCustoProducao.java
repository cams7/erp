/**
 * @version 03/08/2004 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pcp <BR>
 *         Classe: @(#)FRCustoProducao.java <BR>
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

package org.freedom.modulos.pcp.view.frame.report;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRCustoProducao extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtData = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodAlmox = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescAlmox = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodMarca = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldFK txtDescMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtSiglaMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JLabelPad lbOrdem = new JLabelPad( "Ordenar por:" );

	private JRadioGroup<?, ?> rgOrdem = null;

	private JLabelPad lbOpcao = new JLabelPad( "Opção " );

	private JRadioGroup<?, ?> rgOpcao = null;

	private Vector<String> vLbOrdem = new Vector<String>();

	private Vector<String> vVlrOrdem = new Vector<String>();

	private Vector<String> vLbOpcao = new Vector<String>();

	private Vector<String> vVlrOpcao = new Vector<String>();

	private ListaCampos lcAlmox = new ListaCampos( this );

	private ListaCampos lcGrup = new ListaCampos( this );

	private ListaCampos lcMarca = new ListaCampos( this );

	private boolean[] bPrefs = null;

	public FRCustoProducao() {

		setTitulo( "Inventário" );
		setAtribos( 80, 30, 365, 320 );

		GregorianCalendar cal = new GregorianCalendar();
		cal.add( Calendar.DATE, 0 );
		txtData.setVlrDate( cal.getTime() );

		vLbOrdem.addElement( "Código" );
		vLbOrdem.addElement( "Descrição" );
		vVlrOrdem.addElement( "C" );
		vVlrOrdem.addElement( "D" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLbOrdem, vVlrOrdem );
		rgOrdem.setVlrString( "D" );

		vLbOpcao.addElement( "detalhado" );
		vLbOpcao.addElement( "resumido" );
		vVlrOpcao.addElement( "D" );
		vVlrOpcao.addElement( "R" );
		rgOpcao = new JRadioGroup<String, String>( 1, 2, vLbOpcao, vVlrOpcao );
		rgOpcao.setVlrString( "R" );

		lcAlmox.add( new GuardaCampo( txtCodAlmox, "CodAlmox", "Cód.almox.", ListaCampos.DB_PK, false ) );
		lcAlmox.add( new GuardaCampo( txtDescAlmox, "DescAlmox", "Descrição do almox.", ListaCampos.DB_SI, false ) );
		txtCodAlmox.setTabelaExterna( lcAlmox, null );
		txtCodAlmox.setNomeCampo( "CodAlmox" );
		txtCodAlmox.setFK( true );
		lcAlmox.setReadOnly( true );
		lcAlmox.montaSql( false, "ALMOX", "EQ" );

		lcMarca.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca", ListaCampos.DB_PK, false ) );
		lcMarca.add( new GuardaCampo( txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false ) );
		lcMarca.add( new GuardaCampo( txtSiglaMarca, "SiglaMarca", "Sigla", ListaCampos.DB_SI, false ) );
		lcMarca.montaSql( false, "MARCA", "EQ" );
		lcMarca.setReadOnly( true );
		txtCodMarca.setTabelaExterna( lcMarca, null );
		txtCodMarca.setFK( true );
		txtCodMarca.setNomeCampo( "CodMarca" );

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.gurpo", ListaCampos.DB_PK, false ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrup.montaSql( false, "GRUPO", "EQ" );
		lcGrup.setReadOnly( true );
		txtCodGrup.setTabelaExterna( lcGrup, null );
		txtCodGrup.setFK( true );
		txtCodGrup.setNomeCampo( "CodGrup" );

		adic( lbOpcao, 7, 0, 100, 20 );
		adic( rgOpcao, 7, 20, 200, 30 );
		adic( new JLabelPad( "Estoque de:" ), 223, 0, 115, 20 );
		adic( txtData, 223, 20, 115, 20 );
		adic( new JLabelPad( "Cód.almox." ), 7, 60, 80, 20 );
		adic( txtCodAlmox, 7, 80, 80, 20 );
		adic( new JLabelPad( "Descrição do almoxarifado" ), 90, 60, 250, 20 );
		adic( txtDescAlmox, 90, 80, 250, 20 );
		adic( new JLabelPad( "Cód.marca" ), 7, 100, 100, 20 );
		adic( txtCodMarca, 7, 120, 80, 20 );
		adic( new JLabelPad( "Descrição da marca" ), 90, 100, 250, 20 );
		adic( txtDescMarca, 90, 120, 250, 20 );
		adic( new JLabelPad( "Cód.grupo" ), 7, 140, 80, 20 );
		adic( txtCodGrup, 7, 160, 80, 20 );
		adic( new JLabelPad( "Descrição do grupo" ), 90, 140, 250, 20 );
		adic( txtDescGrup, 90, 160, 250, 20 );
		adic( lbOrdem, 7, 185, 80, 15 );
		adic( rgOrdem, 7, 205, 333, 30 );

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSql = null;
		String sCpCodigo = null;
		String sCodMarca = null;
		String sCodGrup = null;
		String sLinhaFina = StringFunctions.replicate( "-", 133 );
		int iCodAlmox = 0;
		int linPag = 0;
		BigDecimal bdCustoTot = new BigDecimal( "0" );
		BigDecimal bdSldProd = new BigDecimal( "0" );
		ImprimeOS imp = null;

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;

			sCpCodigo = ( bPrefs[ 0 ] ? "REFPROD" : "CODPROD" );
			iCodAlmox = txtCodAlmox.getVlrInteger().intValue();
			sCodMarca = txtCodMarca.getVlrString().trim();
			sCodGrup = txtCodGrup.getVlrString().trim();

			sSql = "SELECT " + sCpCodigo + ",DESCPROD,SLDPROD,CUSTOUNIT,CUSTOTOT FROM PPRELCUSTOSP(?,?,?,?,?,?,?,?,?,?,?,?,?) " + " WHERE SLDPROD!=0 " + " ORDER BY " + ( "D".equals( rgOrdem.getVlrString() ) ? "DESCPROD" : sCpCodigo );

			try {

				ps = con.prepareStatement( sSql );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
				ps.setDate( 3, Funcoes.dateToSQLDate( txtData.getVlrDate() ) );

				if ( iCodAlmox != 0 )
					imp.addSubTitulo( "ALMOXARIFADO: " + iCodAlmox + "-" + txtDescAlmox.getVlrString().trim() );

				if ( "".equals( sCodMarca ) ) {
					ps.setNull( 4, Types.INTEGER );
					ps.setNull( 5, Types.SMALLINT );
					ps.setNull( 6, Types.CHAR );
				}
				else {
					ps.setInt( 4, Aplicativo.iCodEmp );
					ps.setInt( 5, ListaCampos.getMasterFilial( "EQMARCA" ) );
					ps.setString( 6, sCodMarca );
					imp.addSubTitulo( "MARCA: " + sCodMarca + "-" + txtDescMarca.getVlrString().trim() );
				}

				if ( "".equals( sCodGrup ) ) {
					ps.setNull( 7, Types.INTEGER );
					ps.setNull( 8, Types.SMALLINT );
					ps.setNull( 9, Types.CHAR );
				}
				else {
					ps.setInt( 7, Aplicativo.iCodEmp );
					ps.setInt( 8, ListaCampos.getMasterFilial( "EQGRUPO" ) );
					ps.setString( 9, sCodGrup );
					imp.addSubTitulo( "GRUPO: " + sCodGrup + "-" + txtDescGrup.getVlrString().trim() );
				}

				ps.setString( 10, "M" );

				if ( iCodAlmox == 0 ) {
					ps.setNull( 11, Types.INTEGER );
					ps.setNull( 12, Types.INTEGER );
					ps.setNull( 13, Types.INTEGER );
				}
				else {
					ps.setInt( 11, Aplicativo.iCodEmp );
					ps.setInt( 12, ListaCampos.getMasterFilial( "EQALMOX" ) );
					ps.setInt( 13, iCodAlmox );
				}

				rs = ps.executeQuery();

				imp.limpaPags();
				imp.montaCab();
				imp.setTitulo( "Relatorio de inventário de estoque" );

				while ( rs.next() ) {

					if ( imp.pRow() >= ( linPag - 1 ) ) {

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "+" + sLinhaFina + "+" );
						imp.incPags();
						imp.eject();

					}

					if ( imp.pRow() == 0 ) {

						imp.impCab( 136, true );

						imp.say( 0, "|" + sLinhaFina + "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "| CODIGO" );
						imp.say( 16, "| DESCRICAO" );
						imp.say( 70, "| SALDO" );
						imp.say( 83, "| CUSTO UNIT." );
						imp.say( 101, "| CUSTO TOTAL" );
						imp.say( 135, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + sLinhaFina + "|" );

					}

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "| " + Funcoes.adicEspacosEsquerda( rs.getString( sCpCodigo ).trim(), 13 ) );
					imp.say( 16, "| " + Funcoes.adicionaEspacos( rs.getString( "DESCPROD" ), 50 ) );
					imp.say( 70, "| " + Funcoes.adicEspacosEsquerda( String.valueOf( rs.getFloat( "SLDPROD" ) ), 10 ) );
					imp.say( 83, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( rs.getFloat( "CUSTOUNIT" ) ) ) );
					imp.say( 101, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( rs.getFloat( "CUSTOTOT" ) ) ) );
					imp.say( 135, "|" );

					bdSldProd = bdSldProd.add( new BigDecimal( rs.getFloat( "SLDPROD" ) ) );
					bdCustoTot = bdCustoTot.add( new BigDecimal( rs.getFloat( "CUSTOTOT" ) ) );

				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" + sLinhaFina + "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "| TOTAL" );
				imp.say( 70, "|" + Funcoes.adicEspacosEsquerda( String.valueOf( Funcoes.arredFloat( bdSldProd.floatValue(), 2 ) ), 10 ) );
				imp.say( 83, "|" );
				imp.say( 101, "|" + Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( bdCustoTot.floatValue() ) ) );
				imp.say( 135, "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "+" + sLinhaFina + "+" );

				imp.eject();
				imp.fechaGravacao();

				rs.close();
				ps.close();
				con.commit();

			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro executando a consulta.\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}

			if ( bVisualizar==TYPE_PRINT.VIEW )
				imp.preview( this );
			else
				imp.print();

		} finally {
			sSql = null;
			sCpCodigo = null;
			sCodMarca = null;
			sCodGrup = null;
			sLinhaFina = null;
			imp = null;
			ps = null;
			rs = null;
			bdCustoTot = null;
			bdSldProd = null;
			linPag = 0;
		}

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcAlmox.setConexao( cn );
		lcGrup.setConexao( cn );
		lcMarca.setConexao( cn );
		bPrefs = getPrefere();

	}

	private boolean[] getPrefere() {

		boolean[] bRetorno = { false };
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";

		try {

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() )
				if ( rs.getString( "UsaRefProd" ) != null )
					if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
						bRetorno[ 0 ] = true;

			rs.close();
			ps.close();
			con.commit();

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			sSQL = null;
			ps = null;
			rs = null;
		}

		return bRetorno;

	}

}
