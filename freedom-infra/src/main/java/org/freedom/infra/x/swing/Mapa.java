package org.freedom.infra.x.swing;

import org.freedom.infra.functions.StringFunctions;
import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.util.GeoUtil;

public class Mapa extends JXMapKit {

	private static final long serialVersionUID = 1L;

	public Mapa() {
		TileFactoryInfo tfi = new GoogleTileFactoryInfo(0, 10, 17, 256, true, true, false);
		setTileFactory(new DefaultTileFactory(tfi));
	}

	public void buscaEndereco(final String rua, final Integer numero, final String cidade, final String uf) {

		GeoPosition posicao = null;
		try {
			if (rua != null && cidade != null && uf != null) {
				System.out.println("Buscando endereço:" + rua.trim() + ", " + ( numero != null ? numero.toString() : 0 ) + "-" + cidade.trim() + "-" + uf);
				posicao = GeoUtil.getPositionForAddress(StringFunctions.clearAccents(rua).trim() + ", " + ( numero != null ? numero.toString() : 0 ), StringFunctions.clearAccents(cidade).trim(), uf);
			}
			else {
				System.out.println("Não foi possível processar o endereço!\nRua, Cidade ou estado, inválidos!");
			}
			setAddressLocation(posicao);
		}
		catch (Exception e) {
			System.out.println("Erro ao processar o endereço!\n" + e.getMessage());
			e.printStackTrace();
		}

	}
}
