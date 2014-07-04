/**
 * 
 */
package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.datos;

import java.sql.ResultSet;
import java.sql.SQLException;

import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;

/**
 * Clase que procesa los atributos de descripcion para la descripcion.
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Datos_Description implements InterfaceChasquiparser{

	
	private LoadCollectionChasqui LCole;

	/**
	 * Constructor de la clase que implementa la descripcion para un objeto virtual.
	 */
	public ElementType_Datos_Description(LoadCollectionChasqui lcole) {
				LCole=lcole;
	}

	@Override
	public void ProcessAttributes() {
		
		
	}

	@Override
	public void ProcessInstances() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT * FROM objeto_virtual;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String Dato=rs.getObject("idov").toString();
					String Dato2="";
					if (rs.getObject("descripcion")!=null)
						Dato2=rs.getObject("descripcion").toString();
					if (Dato!=null&&!Dato.isEmpty()&&Dato2!=null)
						{
						Integer idov=Integer.parseInt(Dato);
						CompleteDocuments S=LCole.getCollection().getObjetoVirtual().get(idov);
						S.setDescriptionText(Dato2);
						
						}
					else System.err.println("Descripcion Vacia");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}


	
	
}
