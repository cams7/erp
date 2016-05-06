/*
 * Created on 17/06/2005 Autor: anderson Descrição:
 */
package org.freedom.modulos.pcp.business.object;

import org.freedom.library.business.object.ModeloLote;

/**
 * @author anderson
 */
public class ModLote extends ModeloLote {

	public ModLote() {

		adicOpcao( "Código do produto", VLR_CODPROD, new Integer( 8 ) );
		adicOpcao( "Dia", VLR_DIA, new Integer( 2 ) );
		adicOpcao( "Mês", VLR_MES, new Integer( 2 ) );
		adicOpcao( "Ano", VLR_ANO, new Integer( 4 ) );
		adicOpcao( "Número da produção no dia", VLR_NPRODDIA, new Integer( 5 ) );
		adicOpcao( "Lote principal", VLR_LOTEPRINC, new Integer( 13 ) );
	}
}
