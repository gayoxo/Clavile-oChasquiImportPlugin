package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.metadatos.catalogo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Clase que parsea los catalogos para los metadatos
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Catalogos implements InterfaceChasquiparser{


	private CompleteElementType catalogos;
	private LoadCollectionChasqui LCole;

	/**
	 * Constructora por parametros de la clase
	 * @param name Nombre del Meta
	 * @param browseable Navegabilidad
	 * @param father padre del Meta
	 */
	public ElementType_Catalogos(
			String name, boolean browseable, CompleteElementType father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		catalogos=new CompleteElementType(name, father);
		
		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		catalogos.getShows().add(VistaOV);
	}

	@Override
	public void ProcessAttributes() {
		processAllCategorias();

	}
	
	@Override
	public void ProcessInstances() {
		
	}

	/**
	 * Funcion que procesa las categorias
	 */
	private void processAllCategorias() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("select contenido, MAX(Total) AS resultado From (Select idov, contenido, count(*) AS total FROM metadatos WHERE ruta='/manifest/metadata/lom/general/catalogentry/catalog' Group By idov,contenido) AS A GROUP BY contenido;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String Dato=rs.getObject("contenido").toString();
					String Dato2=rs.getObject("resultado").toString();
					if (Dato!=null&&!Dato.isEmpty())
						{
						Integer count=Integer.parseInt(Dato2);
						ElementType_Catalogos_Catalogo ATCUnidades=new ElementType_Catalogos_Catalogo(Dato, false, catalogos,LCole);
						ElementType_Catalogos_Catalogo.setCatalogsSons(new ArrayList<CompleteTextElementType>());
						catalogos.getSons().add(ATCUnidades.getExtendMetaControlled());
						ElementType_Catalogos_Catalogo.getCatalogsSons().add(ATCUnidades.getExtendMetaControlled());
						ATCUnidades.ProcessAttributes();
						ArrayList<String> forAll=ATCUnidades.getVocabulario();
						for (int i = 1; i < count; i++) {
							ATCUnidades=new ElementType_Catalogos_Catalogo(Dato, false, catalogos,LCole);
							catalogos.getSons().add(ATCUnidades.getExtendMetaControlled());
							
							LCole.getCollection().getVocabularios().put(ATCUnidades.getExtendMetaControlled(), forAll);
							
							ElementType_Catalogos_Catalogo.getCatalogsSons().add(ATCUnidades.getExtendMetaControlled());
							
						}
						
						ATCUnidades.ProcessInstances();
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * @return the ExtendMeta
	 */
	public CompleteElementType getExtendMeta() {
		return catalogos;
	}

	
	

}
