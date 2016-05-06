/**
 * @version 10/08/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.relatorios <BR>
 *         Classe: @(#)BalanceteBarras.java <BR>
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
 *         Gráfico de balancete financeiro no formato de barras verticais 3D.
 * 
 */

package org.freedom.layout.graphics;

import java.awt.Color;
import java.awt.Font;
import org.freedom.infra.model.jdbc.DbConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.freedom.library.component.LeiauteGR;
import org.freedom.library.functions.Funcoes;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class BalanceteBarras extends LeiauteGR {

	private static final long serialVersionUID = 1L;

	private DbConnection con = null;

	private Font fnTopEmp = new Font( "Arial", Font.BOLD, 11 );

	private Font fnCabEmp = new Font( "Arial", Font.PLAIN, 8 );

	private Font fnCabEmpNeg = new Font( "Arial", Font.BOLD, 8 );

	private DefaultCategoryDataset datasetBar = new DefaultCategoryDataset();

	private ResultSet rs = null;

	private String sTitulo1 = "";

	private String sTitulo2 = "";

	Vector<?> vParamOrc = new Vector<Object>();

	public void montaG() {

		impRaz( false );
		montaCabEmp( con );
		montaRel();
	}

	private JFreeChart createChart( CategoryDataset dataset ) {

		JFreeChart chart = ChartFactory.createBarChart3D( "", // Título
				"Planejamento", // Label X
				"Valores", // Label Y
				dataset, // Dados
				PlotOrientation.VERTICAL, // Orientação
				true, // Legenda
				false, // Tooltips
				false // urls
				);

		chart.setBackgroundPaint( new Color( 255, 255, 255 ) );
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setForegroundAlpha( 0.6f );

		return chart;
	}

	private void montaRel() {

		imprimeRodape( false );
		Vector<Vector<Object>> vData = new Vector<Vector<Object>>();
		double dVlrOutros = 0.0;
		double dVlrTotal = 0.0;
		double dValor = 0.0;
		double dValorPerc = 0.0;
		String sLabel = "";
		try {
			while ( rs.next() ) {
				Vector<Object> vLinha = new Vector<Object>();
				vLinha.addElement( rs.getString( 2 ).trim() );
				vLinha.addElement( new Double( rs.getDouble( 4 ) ) );
				vData.addElement( vLinha );
				dVlrTotal += rs.getDouble( 4 );
			}
		} catch ( SQLException e ) {
			Funcoes.mensagemInforma( this, "Erro na consulta de valores!\n" + e.getMessage() );
		}

		for ( int i2 = 0; vData.size() > i2; i2++ ) {
			dValor = ( (Double) vData.elementAt( i2 ).elementAt( 1 ) ).doubleValue();
			dValorPerc = ( dValor * 100 ) / dVlrTotal;
			if ( dValorPerc < 3.0 ) {
				dVlrOutros += dValor;
			}
			else {
				sLabel = ( (String) vData.elementAt( i2 ).elementAt( 0 ) );
				sLabel = sLabel + " (" + Funcoes.strDecimalToStrCurrency( 14, 2, dValor + "" ) + " ) ";
				datasetBar.addValue( dValor, sLabel, "" );

			}
		}
		if ( dVlrOutros > 0.0 )
			datasetBar.addValue( dVlrOutros, "Outros valores", "" );

		JFreeChart chart = createChart( datasetBar );

		setBordaRel();

		int iY = 35;

		drawLinha( 0, iY, 0, 0, AL_LL );

		iY += 14;

		setFonte( fnTopEmp );
		drawTexto( sTitulo1, 0, iY, getFontMetrics( fnCabEmp ).stringWidth( "  " + sTitulo1 + "  " ), AL_CEN );
		setFonte( fnCabEmpNeg );

		iY += 6;

		drawLinha( 0, iY, 0, 0, AL_LL );

		iY += 14;

		setFonte( fnTopEmp );
		drawTexto( sTitulo2, 0, iY, getFontMetrics( fnCabEmp ).stringWidth( "  " + sTitulo2 + "  " ), AL_CEN );
		setFonte( fnCabEmpNeg );

		iY += 6;

		drawLinha( 0, iY, 0, 0, AL_LL );

		iY += 50;

		drawGrafico( chart, 15, iY, 500, 550 );

		iY += 12;

		setFonte( fnTopEmp );
		drawTexto( "Valor total:" + Funcoes.strDecimalToStrCurrency( 14, 2, dVlrTotal + "" ), 0, iY, getFontMetrics( fnCabEmp ).stringWidth( "  Valor total:" + Funcoes.strDecimalToStrCurrency( 14, 2, dVlrTotal + "" ) + "  " ), AL_CEN );

		termPagina();
		finaliza();

	}

	public void setParam( Vector<?> vParam ) {

		vParamOrc = vParam;
	}

	public void setConsulta( ResultSet rs2 ) {

		rs = rs2;
	}

	public void setTitulo( String sTit1, String sTit2 ) {

		sTitulo1 = sTit1;
		sTitulo2 = sTit2;
	}

	public void setConexao( DbConnection cn ) {

		con = cn;
	}

}
