package com.nullin.hardestgame.map;

import static android.opengl.GLES10.glFrontFace;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Stack;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;

import com.nullin.hardestgame.C;
import com.nullin.hardestgame.engine.ILevelController;
import com.nullin.hardestgame.engine.ResourceManager;
import com.nullin.hardestgame.map.Map.Vertex3D;
import com.nullin.hardestgame.map.data.MapData;
import com.nullin.hardestgame.sprit.Sprite;
import com.nullin.hardestgame.utilites.TextureLoader;

public class Map {

	Paint mPaint;
	private int mapX, mapY, mapWidth, mapHeight;
	private Bitmap mBgImgGrid;// 棋盘格
	private int mapGrid[];// 地图数据
	private Bitmap mWholeMap;
	private Bitmap mWholeMapWithBg;

	private int mLevel;
	private ILevelController levelCon;
	private Rect recordTmp;
	private ArrayList<Rect> records;// 记录点
	private ArrayList<Path> figures;// 边缘
	public Rect start = new Rect(), end = new Rect();

	int wallCount = 0, wayCount = 0, blockCount = 0;

	private ResourceManager mRM;

	public Map() {
		mapX = C.GV_MARGIN_LEFT;
		mapY = C.GV_MARGIN_TOP;
		mapWidth = C.getWidth() - C.GV_MARGIN_LEFT;
		mapHeight = C.getHeight() - C.GV_MARGIN_TOP - C.GV_MARGIN_BOTTOM;

		mRM = ResourceManager.getManager();
		mRM.initGameRes();

		records = new ArrayList<Rect>();
		figures = new ArrayList<Path>();
		initMapGrid();
		initBgPane();

	}

	public Bitmap toGate(int level) {
		if (mWholeMap == null || mLevel != level - 1) {
			this.mLevel = level - 1;
			mWholeMap = Bitmap.createBitmap(mapWidth, mapHeight, Config.ARGB_8888);
			Canvas c = new Canvas(mWholeMap);
			mapGrid = MapData.maps[mLevel];

			Paint ps = new Paint();
			ps.setColor(Color.YELLOW);
			ps.setMaskFilter(new BlurMaskFilter(20, Blur.INNER));

			Paint pr = new Paint();
			pr.setColor(Color.RED);
			pr.setMaskFilter(new BlurMaskFilter(20, Blur.INNER));

			Paint pe = new Paint();
			pe.setColor(Color.GREEN);
			pe.setMaskFilter(new BlurMaskFilter(20, Blur.INNER));

			Paint pb = new Paint();
			pb.setColor(Color.BLACK);

			Paint pba = new Paint();
			pba.setAlpha(40);

			int gridX, gridY;
			for (int i = 0; i < mapGrid.length; i++) {
				gridX = i % C.GRID_W_SIZE * C.GRID_SIZE;
				gridY = i / C.GRID_W_SIZE * C.GRID_SIZE;
				if (mapGrid[i] == C.ZONE_WALL) {
				} else if (mapGrid[i] == C.ZONE_ROAD) {
					c.drawRect(gridX, gridY, gridX + C.GRID_SIZE, gridY + C.GRID_SIZE, pb);
					c.drawBitmap(mBgImgGrid, new Rect(gridX, gridY, gridX + C.GRID_SIZE, gridY + C.GRID_SIZE),
							new Rect(gridX, gridY, gridX + C.GRID_SIZE, gridY + C.GRID_SIZE), pba);

				} else if ((mapGrid[i] & C.ZONE_START) != 0) {
					c.drawRect(gridX, gridY, gridX + C.GRID_SIZE, gridY + C.GRID_SIZE, pb);
					if (!start.contains(gridX, gridY)) {
						start.left = gridX;
						start.top = gridY;
						int endPosition = findZoneEnd(i);
						start.right = endPosition % C.GRID_W_SIZE * C.GRID_SIZE + C.GRID_SIZE;
						start.bottom = endPosition / C.GRID_W_SIZE * C.GRID_SIZE + C.GRID_SIZE;
						records.add(start);
					}
					c.drawRect(gridX, gridY, gridX + C.GRID_SIZE, gridY + C.GRID_SIZE, ps);
				} else if ((mapGrid[i] & C.ZONE_END) != 0) {
					c.drawRect(gridX, gridY, gridX + C.GRID_SIZE, gridY + C.GRID_SIZE, pb);
					if (!end.contains(gridX, gridY)) {
						end.left = gridX;
						end.top = gridY;
						int endPosition = findZoneEnd(i);
						end.right = endPosition % C.GRID_W_SIZE * C.GRID_SIZE + C.GRID_SIZE;
						end.bottom = endPosition / C.GRID_W_SIZE * C.GRID_SIZE + C.GRID_SIZE;
					}
					c.drawRect(gridX, gridY, gridX + C.GRID_SIZE, gridY + C.GRID_SIZE, pe);
				} else if ((mapGrid[i] & C.ZONE_RECORD) != 0) {
					c.drawRect(gridX, gridY, gridX + C.GRID_SIZE, gridY + C.GRID_SIZE, pb);
					if (recordTmp == null || !recordTmp.contains(gridX, gridY)) {
						recordTmp = new Rect();
						recordTmp.left = gridX;
						recordTmp.top = gridY;
						int endPosition = findZoneEnd(i);
						recordTmp.right = endPosition % C.GRID_W_SIZE * C.GRID_SIZE + C.GRID_SIZE;
						recordTmp.bottom = endPosition / C.GRID_W_SIZE * C.GRID_SIZE + C.GRID_SIZE;
						records.add(recordTmp);
					}
					c.drawRect(gridX, gridY, gridX + C.GRID_SIZE, gridY + C.GRID_SIZE, pr);
				}
			}
			initFigure();
			refleshBg();
			initMap3D();
		}
		
		return mWholeMap;
	}

	/**
	 * 生成棋盘格背景
	 */
	public void initMapGrid() {
		Bitmap bm = Bitmap.createBitmap(new int[] { 0x11000000, 0xFFFFFFFF, 0xFFFFFFFF, 0x11000000 }, 2, 2,
				Bitmap.Config.RGB_565);
		Shader bg = new BitmapShader(bm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
		Matrix m = new Matrix();
		m.setScale(C.GRID_SIZE, C.GRID_SIZE);
		bg.setLocalMatrix(m);
		mBgImgGrid = Bitmap.createBitmap(mapWidth, mapHeight, Config.ARGB_8888);
		Canvas c = new Canvas(mBgImgGrid);
		Paint p = new Paint();
		p.setShader(bg);
		c.drawRect(0, 0, mapWidth, mapHeight, p);
	}

	private void initFigure() {
		if (mapGrid == null)
			return;
		figures.clear();
		int grid[][] = new int[C.GRID_H_SIZE][C.GRID_W_SIZE];
		// 为方便操作将数组转化为二维
		for (int i = 0; i < mapGrid.length; i++) {
			grid[i / C.GRID_W_SIZE][i % C.GRID_W_SIZE] = mapGrid[i];
		}
		ArrayList<int[][]> lines = new ArrayList<int[][]>();
		// 找出所有边缘
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == C.ZONE_WALL) {
					if (i + 1 < grid.length && grid[i + 1][j] != 1) {
						lines.add(new int[][] { { j * C.GRID_SIZE, (i + 1) * C.GRID_SIZE },
								{ (j + 1) * C.GRID_SIZE, (i + 1) * C.GRID_SIZE } });
					}
					if (i > 0 && grid[i - 1][j] != 1) {
						lines.add(new int[][] { { (j + 1) * C.GRID_SIZE, i * C.GRID_SIZE },
								{ j * C.GRID_SIZE, i * C.GRID_SIZE } });
					}
					if (j + 1 < grid[0].length && grid[i][j + 1] != 1) {
						lines.add(new int[][] { { (j + 1) * C.GRID_SIZE, (i + 1) * C.GRID_SIZE },
								{ (j + 1) * C.GRID_SIZE, i * C.GRID_SIZE } });
					}
					if (j > 0 && grid[i][j - 1] != 1) {
						lines.add(new int[][] { { j * C.GRID_SIZE, i * C.GRID_SIZE },
								{ j * C.GRID_SIZE, (i + 1) * C.GRID_SIZE } });
					}
				}
			}
		}
		generatePath(lines);

	}

	private void generatePath(ArrayList<int[][]> lines) {
		Path path = null;
		int[][] start, tmp;

		path = new Path();
		while (lines.size() > 0) {
			start = lines.get(0);
			lines.remove(0);
			path.moveTo(mapX + start[0][0], mapY + start[0][1]);
			path.lineTo(mapX + start[1][0], mapY + start[1][1]);
			for (int j = 0; j < lines.size(); j++) {
				tmp = lines.get(j);
				if (start[1][0] == tmp[0][0] && start[1][1] == tmp[0][1]) {
					path.lineTo(mapX + tmp[1][0], mapY + tmp[1][1]);
					start[1] = tmp[1];
					lines.remove(j);
					j = -1;
				}
			}
			path.lineTo(mapX + start[0][0], mapY + start[0][1]);
			figures.add(path);
		}
		lines.clear();
		lines = null;
	}

	public boolean canPass(Sprite sprite, float x, float y) {
		if (x < 0 || y < 0 || x > C.getWidth() || y > C.getHeight())
			return false;
		int state = mapGrid[((int) x - C.GV_MARGIN_LEFT) / C.GRID_SIZE + (((int) y - C.GV_MARGIN_TOP) / C.GRID_SIZE)
				* C.GRID_W_SIZE];
		if ((state & C.ZONE_WALL) != 0) {
			return false;
		} else if ((state & C.ZONE_END) != 0) {
			if (levelCon != null) {
				levelCon.levelComplete();
			}
		} else if ((state & C.ZONE_RECORD) != 0 || (state & C.ZONE_START) != 0) {
			for (int i = 0; i < records.size(); i++) {
				recordTmp = records.get(i);
				if (recordTmp.contains((int) x - C.GV_MARGIN_LEFT, (int) y - C.GV_MARGIN_TOP)) {
					sprite.recordPosition(recordTmp.centerX() + C.GV_MARGIN_LEFT, recordTmp.exactCenterY()
							+ C.GV_MARGIN_TOP);
					break;
				}
			}
		}
		return true;
	}

	public void registerLevelController(ILevelController lc) {
		this.levelCon = lc;
	}

	/**
	 * To generate the right-bottom index of zone specified
	 * 
	 * @param point
	 * @return
	 */
	private int findZoneEnd(int point) {
		int zoneType = mapGrid[point];
		if (point + 1 < mapGrid.length && mapGrid[point + 1] == zoneType) {
			point = findZoneEnd(point + 1);
		} else if (point + C.GRID_W_SIZE < mapGrid.length && mapGrid[point + C.GRID_W_SIZE] == zoneType) {
			point = findZoneEnd(point + C.GRID_W_SIZE);
		}
		return point;
	}

	private void refleshBg() {
		mWholeMapWithBg = Bitmap.createBitmap(C.getWidth(), C.getHeight(), Config.ARGB_4444);
		Canvas c = new Canvas(mWholeMapWithBg);
		c.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG));
		c.drawBitmap(mRM.rGameBg, 0, 0, null);

		Paint p = new Paint();
		p.setColor(Color.CYAN);
		p.setMaskFilter(new BlurMaskFilter(12, Blur.OUTER));
		p.setAntiAlias(true);
		p.setDither(true);
		p.setStyle(Paint.Style.STROKE);

		for (int i = 0; i < figures.size(); i++) {
			c.drawPath(figures.get(i), p);
		}
		drawMap(c);
		c = null;

	}

	public void drawMap(Canvas canvas) {
		canvas.drawBitmap(mWholeMap, mapX, mapY, null);
	}

	public void drawMapWithBg(Canvas canvas) {
		canvas.drawBitmap(mWholeMapWithBg, 0, 0, null);
	}

	public void drawMapWithBg(GL10 gl) {
		glFrontFace(GL10.GL_CCW);

		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//		TextureLoader.activeTexture(0);
//		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
//		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bgVertexBuffer);
//		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoorBuffer);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, wallBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, wallCount);
		TextureLoader.activeTexture(2);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoorBuffer);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, wayBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, wayCount);
		TextureLoader.activeTexture(5);

		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoorBuffer);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, blockBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, blockCount);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

	}

	public int getHeight() {
		return mapHeight;
	}

	public int getWidth() {
		return mapWidth;
	}

	private FloatBuffer bgVertexBuffer;
	private FloatBuffer texBuffer;

	public void initBgPane() {
		float[] vertex = new float[] { -15f, 9f, -3, -15f, -9f, -3,

		15f, 9f, -3, 15f, -9f, -3, };
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertex.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		bgVertexBuffer = vbb.asFloatBuffer();
		bgVertexBuffer.put(vertex);
		bgVertexBuffer.position(0);
		
		final float X = 0.78125f, Y = 0.9375f;
		float[] texture = new float[] { 0f, 0, 0f, Y,
		X, 0, X, Y, };
		ByteBuffer tbb = ByteBuffer.allocateDirect(texture.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texture);
		texBuffer.position(0);
	}
	
	private FloatBuffer wallBuffer;
	private FloatBuffer wayBuffer;
	private FloatBuffer blockBuffer;
	private FloatBuffer texCoorBuffer;

	public void initMap3D() {
		int mapWidth = 22;
		int mapHeight = 14;
		float tileSize = 1.1f;// 0.32f;
		float wallHeight = 1.0f;
		Stack<Vertex3D> wayList = new Stack<Vertex3D>();
		Stack<Vertex3D> wallList = new Stack<Vertex3D>();
		Stack<Vertex3D> blockList = new Stack<Vertex3D>();

		wallCount = 0;
		wayCount = 0;
		blockCount = 0;
		
		float width = mapWidth * tileSize;
		float height = mapHeight * tileSize;
		for (int i = 0; i < mapHeight; i++) {
			for (int j = 0; j < mapWidth; j++) {
				float x = -j * tileSize - width / 2+26;
				float y = i * tileSize - height / 2+1;
				float z = -2f;
				int index = i * mapWidth + j;
				if (mapGrid[index] == 1) {
					pushNode(blockList, Vertex3DMake(x, y, z));
					pushNode(blockList, Vertex3DMake(x, y + tileSize, z));
					pushNode(blockList, Vertex3DMake(x + tileSize, y, z));
					pushNode(blockList, Vertex3DMake(x, y + tileSize, z));
					pushNode(blockList, Vertex3DMake(x + tileSize, y + tileSize, z));
					pushNode(blockList, Vertex3DMake(x + tileSize, y, z));
					blockCount += 6;
				} else {
					pushNode(wayList, Vertex3DMake(x, y, z - wallHeight));
					pushNode(wayList, Vertex3DMake(x, y + tileSize, z - wallHeight));
					pushNode(wayList, Vertex3DMake(x + tileSize, y, z - wallHeight));
					pushNode(wayList, Vertex3DMake(x, y + tileSize, z - wallHeight));
					pushNode(wayList, Vertex3DMake(x + tileSize, y + tileSize, z - wallHeight));
					pushNode(wayList, Vertex3DMake(x + tileSize, y, z - wallHeight));
					wayCount += 6;
					if (j % mapWidth > 0 && 1 == mapGrid[index - 1]) {
						pushNode(wallList, Vertex3DMake(x, y, z));
						pushNode(wallList, Vertex3DMake(x, y, z - wallHeight));
						pushNode(wallList, Vertex3DMake(x, y + tileSize, z));

						pushNode(wallList, Vertex3DMake(x, y, z - wallHeight));
						pushNode(wallList, Vertex3DMake(x, y + tileSize, z - wallHeight));
						pushNode(wallList, Vertex3DMake(x, y + tileSize, z));
						wallCount += 6;
					}
					if (j % mapWidth < mapWidth - 1 && 1 == mapGrid[index + 1]) {
						pushNode(wallList, Vertex3DMake(x + tileSize, y, z));
						pushNode(wallList, Vertex3DMake(x + tileSize, y, z - wallHeight));
						pushNode(wallList, Vertex3DMake(x + tileSize, y + tileSize, z));
						pushNode(wallList, Vertex3DMake(x + tileSize, y, z - wallHeight));
						pushNode(wallList, Vertex3DMake(x + tileSize, y + tileSize, z - wallHeight));
						pushNode(wallList, Vertex3DMake(x + tileSize, y + tileSize, z));
						wallCount += 6;
					}
					if (i > 0 && 1 == mapGrid[index - mapWidth]) {
						pushNode(wallList, Vertex3DMake(x, y, z));
						pushNode(wallList, Vertex3DMake(x, y, z - wallHeight));
						pushNode(wallList, Vertex3DMake(x + tileSize, y, z));
						pushNode(wallList, Vertex3DMake(x, y, z - wallHeight));
						pushNode(wallList, Vertex3DMake(x + tileSize, y, z - wallHeight));
						pushNode(wallList, Vertex3DMake(x + tileSize, y, z));
						wallCount += 6;
					}
					if (i < mapHeight - 1 && 1 == mapGrid[index + mapWidth]) {
						pushNode(wallList, Vertex3DMake(x, y + tileSize, z));
						pushNode(wallList, Vertex3DMake(x, y + tileSize, z - wallHeight));
						pushNode(wallList, Vertex3DMake(x + tileSize, y + tileSize, z));
						pushNode(wallList, Vertex3DMake(x, y + tileSize, z - wallHeight));
						pushNode(wallList, Vertex3DMake(x + tileSize, y + tileSize, z - wallHeight));
						pushNode(wallList, Vertex3DMake(x + tileSize, y + tileSize, z));
						wallCount += 6;
					}
				}
			}
		}
		
		  	float wall[] = new float[wallCount * 3];
		    for (int i = 0; i < wallCount; i++) {
		    	Vertex3D v =popNode(wallList);
		        wall[3 * i + 0] = v.x;
		        wall[3 * i + 1] = v.y;
		        wall[3 * i + 2] = v.z;
		    }
			ByteBuffer vbb = ByteBuffer.allocateDirect(wall.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			wallBuffer = vbb.asFloatBuffer();
			wallBuffer.put(wall);
			wallBuffer.position(0);

		    float way[] = new float[wayCount * 3];
		    for (int i = 0; i < wayCount; i++) {
		    	Vertex3D v =popNode(wayList);
		        way[3 * i + 0] = v.x;
		        way[3 * i + 1] = v.y;
		        way[3 * i + 2] = v.z;
 		    }
		    
		    vbb = ByteBuffer.allocateDirect(way.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			wayBuffer = vbb.asFloatBuffer();
			wayBuffer.put(way);
			wayBuffer.position(0);
		    
		    float block[] =  new float[blockCount * 3];
		    for (int i = 0; i < blockCount; i++) {
		    	Vertex3D v = popNode(blockList);
		        block[3 * i + 0] = v.x;
		        block[3 * i + 1] = v.y;
		        block[3 * i + 2] = v.z;
		    }
		    vbb = ByteBuffer.allocateDirect(block.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			blockBuffer = vbb.asFloatBuffer();
			blockBuffer.put(block);
			blockBuffer.position(0);
		    int textCount = Math.max(blockCount, Math.max(wallCount, wayCount)) * 2;
		    float texCoor[] = new float[textCount];
		    float texCoorTmp [] = {
		        0.0f, 0.0f,
		        0.0f, 1.0f,
		        1.0f, 1.0f,
		        
		        0.0f, 0.0f,       
		        1.0f, 1.0f,
		        1.0f, 0.0f
		    };
		    for (int i = 0; i < textCount; i++) {
		        texCoor[i] = texCoorTmp[i % 12];
		    }
		    vbb = ByteBuffer.allocateDirect(textCount * 4);
			vbb.order(ByteOrder.nativeOrder());
			texCoorBuffer = vbb.asFloatBuffer();
			texCoorBuffer.put(texCoor);
			texCoorBuffer.position(0);
	}

	public void pushNode(Stack<Vertex3D> s, Vertex3D v) {
		s.push(v);
	}
	public Vertex3D popNode(Stack<Vertex3D> s){
		return (Vertex3D)s.pop();
	}

	class Vertex3D {
		float x, y, z;
	}

	public Vertex3D Vertex3DMake(float x, float y, float z) {
		Vertex3D v = new Vertex3D();
		v.x = x;
		v.y = y;
		v.z = z;
		return v;
	}

}
