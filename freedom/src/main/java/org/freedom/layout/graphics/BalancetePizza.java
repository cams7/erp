/**
 * @version 10/08/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.relatorios <BR>
 *         Classe: @(#)BalancetePizza.java <BR>
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
 *         Gráfico de balancete financeiro no formato de pizza 3D.
 * 
 */

package org.freedom.layout.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.freedom.infra.model.jdbc.DbConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.Timer;

import org.freedom.library.component.LeiauteGR;
import org.freedom.library.functions.Funcoes;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

public class BalancetePizza extends LeiauteGR {

	private static final long serialVersionUID = 1L;

	private DbConnection con = null;

	private Font fnTopEmp = new Font( "Arial", Font.BOLD, 11 );

	private Font fnCabEmp = new Font( "Arial", Font.PLAIN, 8 );

	private Font fnCabEmpNeg = new Font( "Arial", Font.BOLD, 8 );

	private Font fnLabel = new Font( "Arial", Font.BOLD, 10 );

	private ResultSet rs = null;

	private String sTitulo1 = "";

	private String sTitulo2 = "";

	private String sVlrLabel = "";

	private JFreeChart chart = null;

	private boolean bGirar = false;

	Vector<?> vParamOrc = new Vector<Object>();

	public void montaG() {

		impRaz( false );
		montaCabEmp( con );
		montaRel();
	}

	private JFreeChart createChart( DefaultPieDataset dataset ) {

		JFreeChart chart = ChartFactory.createPieChart3D( "", dataset, true, false, false );

//		chart.setBackgroundPaint( new Color( 255, 255, 255 ) );
		
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setForegroundAlpha( 0.60f );
		plot.setBackgroundPaint( Color.WHITE );
		
		if ( bGirar ) {
		
			plot.setStartAngle( 270 );
			plot.setDirection( Rotation.ANTICLOCKWISE );
			
// 			Parâmetro comentado, pois estava causando bug no gráfico			
//			plot.setInteriorGap( 0.33 );
			Rotator rotator = new Rotator( plot );
			rotator.start();
			
		}

		plot.setLabelFont( fnLabel );
		plot.setOutlineStroke( null );

		return chart;
	}

	private void montaRel() {

		imprimeRodape( false );
		DefaultPieDataset data = new DefaultPieDataset();
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
				data.setValue( sLabel, dValor );
			}
		}
		if ( dVlrOutros > 0.0 )
			data.setValue( "Outros valores (" + Funcoes.strDecimalToStrCurrency( 14, 2, dVlrOutros + "" ) + " ) ", dVlrOutros );

		chart = createChart( data );

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

		drawGrafico( chart, 15, iY, 550, 400 );

		iY += 12;

		setFonte( fnTopEmp );
		sVlrLabel = "Valor total:" + Funcoes.strDecimalToStrCurrency( 14, 2, dVlrTotal + "" );
		drawTexto( sVlrLabel, 0, iY, getFontMetrics( fnCabEmp ).stringWidth( "  " + sVlrLabel + "  " ), AL_CEN );

		termPagina();
		finaliza();

	}

	public void setParam( Vector<?> vParam ) {

		vParamOrc = vParam;
	}

	public JFreeChart getGrafico() {

		return chart;
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

	public void setGirar( boolean bGir ) {

		bGirar = bGir;
	}

	public String getVlrLabel() {

		return sVlrLabel;
	}

	class Rotator extends Timer implements ActionListener {

		private static final long serialVersionUID = 1L;

		private PiePlot3D plot;

		private int angle = 270;

		Rotator( PiePlot3D plot ) {

			super( 100, null );
			this.plot = plot;
			addActionListener( this );
		}

		public void actionPerformed( ActionEvent event ) {

			this.plot.setStartAngle( angle );
			this.angle = this.angle + 2;
			if ( this.angle == 360 ) {
				this.angle = 0;
			}
		}
	}
}
