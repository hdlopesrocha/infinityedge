package pt.hidrogine.infinityedge.model;

import android.content.Context;

import hidrogine.math.BoundingSphere;
import hidrogine.math.Camera;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import hidrogine.math.IBoundingSphere;
import hidrogine.math.IModel3D;
import hidrogine.math.IVector3;
import hidrogine.math.Vector3;
import pt.hidrogine.infinityedge.util.Material;
import pt.hidrogine.infinityedge.util.ShaderProgram;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;


public class Model3D extends Model implements IModel3D{

    public ArrayList<Group> groups = new ArrayList<Group>();
    private IBoundingSphere container;

    public Model3D(final Context context, final int resource, final float scale) {


        TreeMap<String, Material> materials = new TreeMap<String, Material>();
        try {
  /*
            JSONObject jObject = new JSONObject(FileString.read(context, resource));
            {
                JSONObject jMats = jObject.getJSONObject("materials");

                Iterator<String> iterator = jMats.keys();

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    JSONObject jMat = jMats.getJSONObject(key);

                    Material currentMaterial = new Material();
                    materials.put(key, currentMaterial);

                    if (jMat.has("map_Kd")) {
                        currentMaterial.setTexture(context, jMat.getString("map_Kd"));
                    }
                    if (jMat.has("Ka")) {
                        JSONArray array = jMat.getJSONArray("Ka");
                        currentMaterial.Ka = new Float[3];
                        for (int j = 0; j < 3; ++j)
                            currentMaterial.Ka[j] = (float) array.getDouble(j);
                    }
                    if (jMat.has("Kd")) {
                        JSONArray array = jMat.getJSONArray("Kd");
                        currentMaterial.Kd = new Float[3];
                        for (int j = 0; j < 3; ++j)
                            currentMaterial.Kd[j] = (float) array.getDouble(j);
                    }
                    if (jMat.has("Ks")) {
                        JSONArray array = jMat.getJSONArray("Ks");
                        currentMaterial.Ks = new Float[3];
                        for (int j = 0; j < 3; ++j)
                            currentMaterial.Ks[j] = (float) array.getDouble(j);
                    }
                    if (jMat.has("Tf")) {
                        currentMaterial.Tf = (float) jMat.getDouble("Tf");
                    }
                    if (jMat.has("illum")) {
                        currentMaterial.illum = (float) jMat.getDouble("illum");
                    }
                    if (jMat.has("d")) {
                        currentMaterial.d = (float) jMat.getDouble("d");
                    }
                    if (jMat.has("Ns")) {
                        currentMaterial.Ns = (float) jMat.getDouble("Ns");
                    }
                    if (jMat.has("sharpness")) {
                        currentMaterial.sharpness = (float) jMat.getDouble("sharpness");
                    }
                    if (jMat.has("Ni")) {
                        currentMaterial.Ni = (float) jMat.getDouble("Ni");
                    }

                }
            }
*/

            JsonParser jsonParser = new JsonFactory().createParser(context.getResources().openRawResource(resource));
            //loop through the JsonTokens
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String name = jsonParser.getCurrentName();
                if ("materials".equals(name)) {
                    jsonParser.nextToken();
                    while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                        String materialName = jsonParser.getCurrentName();
                        Material currentMaterial = new Material();
                        materials.put(materialName, currentMaterial);
                        jsonParser.nextToken();
                        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                            String key = jsonParser.getCurrentName();
                            jsonParser.nextToken();
                            String value = jsonParser.getValueAsString();
                            if ("map_Kd".equals(key)) {
                                currentMaterial.setTexture(context, value);
                            }
                            System.out.println(materialName + " | " + key + " -> " + value);
                        }
                    }
                } else if ("groups".equals(name)) {
                    final List<IVector3> points = new ArrayList<IVector3>();
                    jsonParser.nextToken(); // {
                    while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                        String groupName = jsonParser.getCurrentName();
                        jsonParser.nextToken(); // [
                        Group currentGroup = new Group();
                        groups.add(currentGroup);
                        currentGroup.name = groupName;
                        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                            jsonParser.nextToken(); // {
                            BufferObject currentSubGroup = new BufferObject();
                            currentGroup.subGroups.add(currentSubGroup);
                            System.out.println("JSON INIT");
                            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                                String key = jsonParser.getCurrentName();
                                if ("mm".equals(key)) {
                                    String mm = jsonParser.getValueAsString();
                                    currentSubGroup.setMaterial(materials.get(mm));
                                } else if ("vv".equals(key)) {
                                    int k = 0;
                                    float x=0, y=0, z=0;

                                    jsonParser.nextToken(); // [
                                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                                        float val = jsonParser.getFloatValue() * scale;
                                        switch (k){
                                            case 0 : x = val; break;
                                            case 1 : y = val; currentGroup.maxY = Math.max(currentGroup.maxY, val);break;
                                            case 2 : z = val;break;
                                        }
                                        if(k==2){
                                            k=0;
                                            points.add(new Vector3(x,y,z));
                                        }
                                        else {
                                            ++k;
                                        }
                                        currentSubGroup.addVertex(val);

                                    }
                                } else if ("vn".equals(key)) {
                                    jsonParser.nextToken(); // [
                                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                                        float val = jsonParser.getFloatValue();
                                        currentSubGroup.addNormal(val);
                                    }
                                } else if ("vt".equals(key)) {
                                    jsonParser.nextToken(); // [
                                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                                        float val = jsonParser.getFloatValue();
                                        currentSubGroup.addTexture(val);
                                    }
                                } else if ("ii".equals(key)) {
                                    jsonParser.nextToken(); // [
                                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                                        short val = jsonParser.getShortValue();
                                        currentSubGroup.addIndex(val);
                                    }
                                }
                            }
                            System.out.println("JSON END");
                            currentSubGroup.buildBuffer();
                            System.out.println("JSON BUILD BUFFER");
                        }
                    }
                    container = new BoundingSphere().createFromPoints(points);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(ShaderProgram shader, Camera camera) {
        for (Group g : groups) {
            for (BufferObject sg : g.subGroups) {
                sg.draw(shader);
            }
        }
    }

    @Override
    public IBoundingSphere getContainer() {
        return container;
    }
}
