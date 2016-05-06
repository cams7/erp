/**
 * @version 10/08/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.relatorios <BR>
 *         Classe:
 * @(#)EvoluVendasBarras.java <BR>
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
 *                            Gráfico de evolução de vendas no formato de barras verticais 3D.
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

public class EvoluVendasBarras extends LeiauteGR {

	private static final long serialVersionUID = 1L;

	private DbConnection con = null;

	private Font fnTopEmp = new Font( "Arial", Font.BOLD, 11 );

	private Font fnCabEmp = new Font( "Arial", Font.PLAIN, 8 );

	private Font fnCabEmpNeg = new Font( "Arial", Font.BOLD, 8 );

	private DefaultCategoryDataset datasetBar = new DefaultCategoryDataset();

	private ResultSet rs = null;

	Vector<?> vParamOrc = new Vector<Object>();

	public void montaG() {

		impRaz( false );
		montaCabEmp( con );
		montaRel();
	}

	private JFreeChart createChart( CategoryDataset dataset ) {

		JFreeChart chart = ChartFactory.createBarChart3D( "", // Título
				"Meses", // Label X
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
		try {

			while ( rs.next() ) {

				datasetBar.addValue( rs.getDouble( 1 ), Funcoes.adicionaEspacos( Funcoes.strMes( rs.getInt( 2 ) ), 3 ) + "/" + rs.getString( 3 ) + " >" + Funcoes.strDecimalToStrCurrency( 14, 2, rs.getString( 1 ) + " ) " ), "" );
			}

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemInforma( this, "Erro na consulta de valores!\n" + e.getMessage() );
		}

		JFreeChart chart = createChart( datasetBar );

		setBordaRel();

		int iY = 35;

		drawLinha( 0, iY, 0, 0, AL_LL );

		iY += 14;

		setFonte( fnTopEmp );
		drawTexto( "EVOLUÇÃO DE VENDAS", 0, iY, getFontMetrics( fnCabEmp ).stringWidth( "  EVOLUÇÃO DE VENDAS  " ), AL_CEN );
		setFonte( fnCabEmpNeg );

		iY += 6;

		drawLinha( 0, iY, 0, 0, AL_LL );

		iY += 50;

		drawGrafico( chart, 15, iY, 500, 500 );

		termPagina();
		finaliza();
	}

	public void setParam( Vector<?> vParam ) {

		vParamOrc = vParam;
	}

	public void setConsulta( ResultSet rs2 ) {

		rs = rs2;
	}

	public void setConexao( DbConnection cn ) {

		con = cn;
	}

}
