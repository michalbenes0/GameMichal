package renderEngine.terrains;

import java.awt.Image;
import java.awt.dnd.DnDConstants;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.BufferedImageUtil;

import renderEngine.HeightsGenerator;
import renderEngine.Loader;
import renderEngine.models.RawModel;
import renderEngine.textures.ModelTexture;
import utils.math.barryCentric;

public class Terrain {

    private static final int CHUNK_SIZE = 1024;
    private static final int SIZE = CHUNK_SIZE * 3;
    //private static final int VERTEX_COUNT = 256;
    private static final int MAX_HEIGHT = 40;
    private static final int MIN_HEIGHT = 0;
    private static final int MAX_PIXEL_COLOR = 256 * 256 * 256;
    public HeightsGenerator generator = new HeightsGenerator();
    private float x;
    private float z;
    private RawModel model;
    private ModelTexture texture;
    private int[] indices;
    private float[] textureCoords;
    private Loader loader;
//    private int[][] chunks;
    public float[][] heights;
    private String pathToDB = "jdbc:h2:file:./world/db.mv.db";
    private Connection conn;
    private int dx = 0;
    private int dz = 0;

    public Terrain(int gridX, int gridZ, Loader loader, ModelTexture texture) {
        this.texture = texture;
        this.x = gridX;
        this.z = gridZ;
        this.loader = loader;
        try {
            //        generateHeightMap();
            loadDB();
        } catch (SQLException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadHeightMap();
        this.model = generateTerrain(loader, "heightMap");
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public ModelTexture getTexture() {
        return texture;
    }

    /*private void loadChunksPositions(File file) {
        try (RandomAccessFile input = new RandomAccessFile(file,"r");) {
            int chunkSize = 1024 * 1024 * 4;
            for (int i=0;i<=input.length()/(chunkSize+);i++){
                input.
            }
        } catch (IOException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
    private void saveHeightsMap(File file, float[][] heights) {
//        try ( DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(file));) {
        try ( RandomAccessFile output = new RandomAccessFile(file,"w");) {
            output.seek(0);
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    output.writeFloat(heights[i][j]);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadDB() throws SQLException {
        Connection conn = DriverManager.getConnection(pathToDB);
        this.conn = conn;
    }
    
    private float[][] getChunk(int x,int z) throws SQLException, IOException {
        var pstmt = conn.prepareStatement("SELECT CHUNKDATA FROM CHUNKS where POSX = ? and POSZ = ?;");
        pstmt.setInt(1, x);
        pstmt.setInt(2, z);
        var resultSet = pstmt.executeQuery();
        float[][] answer = new float[CHUNK_SIZE][CHUNK_SIZE];
        while (resultSet.next()) {
            DataInputStream input = new DataInputStream(resultSet.getBlob("CHUNKDATA").getBinaryStream());
            //System.out.println(input.readFloat());
            for (int i=0;i<CHUNK_SIZE;i++) {
                for (int j=0;j<CHUNK_SIZE;j++) {
                    answer[i][j] = input.readFloat();
                }
            }
        }
        pstmt.close();
        return answer;
    }
    
    private void dropDB() throws SQLException {
        this.conn.close();
    }
    
    private void loadHeightMap() {
        
            //            RandomAccessFile heightsFile = new RandomAccessFile("/world/heights.dat", "r");
            /*
            File file = new File("world/heights.dat");
            if (!file.exists()) {
            generateHeightMap();
            saveHeightsMap(file, heights);
            return;
            }
            float[][] newHeights = new float[SIZE][SIZE];
            //float[] tmpFloat = new float[];
            
            System.out.print("Loading world from files...  ");
            //        try ( DataInputStream input = new DataInputStream(new ByteArrayInputStream(Files.readAllBytes(file.toPath())));) {
            try ( RandomAccessFile input = new RandomAccessFile(file,"r");) {
            input.seek(0);

            for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
            try {
            newHeights[i][j] = input.readFloat();
            } catch (IOException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            }
            
            }
            } catch (IOException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Done");
            this.heights = newHeights;
            */
        int camX = 0;
        int camZ = 0;
        int gridX = Math.max(1, (int) camX % CHUNK_SIZE);
        int gridZ = Math.max(1, (int) camZ % CHUNK_SIZE);
        float[][] heightsTmp = new float[SIZE][SIZE];
        try {
            float[][][][] chunks = new float[3][3][CHUNK_SIZE][CHUNK_SIZE];
            chunks[0][0] = getChunk(gridX-1, gridZ-1);
            chunks[0][1] = getChunk(gridX  , gridZ-1);
            chunks[0][2] = getChunk(gridX+1, gridZ-1);
            chunks[1][0] = getChunk(gridX-1, gridZ  );
            chunks[1][1] = getChunk(gridX  , gridZ  );
            chunks[1][2] = getChunk(gridX+1, gridZ  );
            chunks[2][0] = getChunk(gridX-1, gridZ+1);
            chunks[2][1] = getChunk(gridX  , gridZ+1);
            chunks[2][2] = getChunk(gridX+1, gridZ+1);
            
            for (int i=0;i < SIZE;i++) {
                for (int j=0;j < SIZE;j++) {
                    try {
                        heightsTmp[i][j] = chunks[(int)i / CHUNK_SIZE][(int)j / CHUNK_SIZE][i % CHUNK_SIZE][j % CHUNK_SIZE];
                    } catch (Exception e) {
                        System.out.println(Integer.toString(i)+"   "+Integer.toString(j));
                        e.printStackTrace();
                    }
                }
            }
            this.heights = heightsTmp;
            this.dx = (gridX - 1) * CHUNK_SIZE; 
            this.dz = (gridZ - 1) * CHUNK_SIZE;
        } catch (SQLException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void generateHeightMap() {
        heights = new float[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                heights[i][j] = getHeight(i, j);
            }
        }
    }

    public float getHeightOfTerrain(float worldX, float worldZ) {
        float terrainX = worldX - (this.x + dx);
        float terrainZ = worldZ - (this.z + dz);
        float gridSquareSize = SIZE / heights.length;
        //int gridX = (int) Math.floor(terrainX/gridSquareSize);
        //int gridZ = (int) Math.floor(terrainZ/gridSquareSize);
        int gridX = (int) (terrainX / gridSquareSize);
        int gridZ = (int) (terrainZ / gridSquareSize);
        if (gridX > heights.length - 2
                || gridZ > heights.length - 2
                || gridX < 0
                || gridZ < 0) {
            return 0;
        }

        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float answer;

        if (xCoord <= (1 - zCoord) && xCoord <= 1 && xCoord >= 0) {
            answer = barryCentric
                    .barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ], 0), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else if (xCoord > (1 - zCoord) && xCoord <= 1 && xCoord >= 0) {
            answer = barryCentric
                    .barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            answer = 0;
        }
        return answer;
    }

    private void regenerateVertices() {
        int count = SIZE * SIZE;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        int vertexPointer = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                vertices[vertexPointer * 3] = (float) j + dx;
                vertices[vertexPointer * 3 + 1] = heights[j][i];
                vertices[vertexPointer * 3 + 2] = (float) i + dz;
                Vector3f normal = calculateNormal(j, i);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;
                vertexPointer++;
            }
        }
        this.model = loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private RawModel generateTerrain(Loader loader, String heightMap) {

        //int VERTEX_COUNT = 256;
        int count = SIZE * SIZE;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[(6 * (SIZE - 1) * (SIZE - 1))];
        int vertexPointer = 0;
        //for(int i=0;i<SIZE;i++){
        //	for(int j=0;j<SIZE;j++){
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                //vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer * 3] = (float) j;
                //float height = getHeight(j, i);
                //heights[i][j] = height;
                vertices[vertexPointer * 3 + 1] = heights[j][i];
                //vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer * 3 + 2] = (float) i;
                Vector3f normal = calculateNormal(j, i);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;
                textureCoords[vertexPointer * 2] = (float) j / ((float) SIZE - 1);
                //textureCoords[vertexPointer*2] = (float)j;
                textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) SIZE - 1);
                //textureCoords[vertexPointer*2+1] = (float)i;
                vertexPointer++;
            }
        }
        int pointer = 0;

        for (int gz = 0; gz < SIZE - 1; gz++) {
            for (int gx = 0; gx < SIZE - 1; gx++) {
                int topLeft = (gz * SIZE) + gx;
                //int topLeft = (gz)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = (((gz + 1) * SIZE) + gx);
                //int bottomLeft = ((gz+1))+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        this.indices = indices;
        this.textureCoords = textureCoords;

        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private Vector3f calculateNormal(int x, int z) {
//        float heightL = getHeight(x - 1, z);
//        float heightR = getHeight(x + 1, z);
//        float heightD = getHeight(x, z - 1);
//        float heightU = getHeight(x, z + 1);
        float heightL;
        float heightR;
        float heightD;
        float heightU;
        try {
            heightL = heights[x - 1][z];
        } catch(Exception e) {
            heightL = getHeight(x - 1 + dx, z + dz);
        }
        try {
            heightR= heights[x + 1][z];
        } catch(Exception e) {
            heightR = getHeight(x + 1 + dx, z + dz);
        }
        try {
            heightD = heights[x][z - 1];
        } catch(Exception e) {
            heightD = getHeight(x + dx, z - 1 + dz);
        }
        try {
            heightU = heights[x][z + 1];
        } catch(Exception e) {
            heightU = getHeight(x + dx, z + 1  + dz);
        }
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }

    /*
	public float getHeight(int x,int z,BufferedImage image) {
		if (
			x < 0 ||
			x >= image.getHeight() ||
			z < 0 ||
			z >= image.getHeight()
			) 
		{return (float) 0;}
		
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOR/2f;
		height /= MAX_PIXEL_COLOR/2f;
		height *= MAX_HEIGHT;

		return height;
		
	}
     */
    public float getHeight(int x, int z) {
        return generator.generateHeight(x, z);

    }

}
