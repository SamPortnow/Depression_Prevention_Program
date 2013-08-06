package com.samportnow.bato.coping.rollingball;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Ball
{
	FloatBuffer m_VertexData;
	FloatBuffer m_NormalData;
	FloatBuffer m_ColorData;
	
	float m_Scale;
	float m_Squash;
	float m_Radius;
	int m_Stacks, m_Slices;
	
	public Ball(int stacks, int slices, float radius, float squash)
	{
		this.m_Stacks = stacks;
		this.m_Slices = slices;
		this.m_Radius = radius;
		this.m_Squash = squash;
		init(m_Stacks, m_Slices, radius, squash);
	}


	private void init(int stacks, int slices, float radius, float squash)
	{
		float [] vertexData;
		float [] colorData;
		
		float colorIncrement=0f;
		
		float blue=0f;
		float red=1.0f;
		int vIndex = 0;
		int cIndex = 0;
		
		m_Scale=radius;
		m_Squash=squash;
		
		colorIncrement=1.0f/(float)stacks;
		
		{
			m_Stacks = stacks;
			m_Slices = slices;
			
			vertexData = new float[3*((m_Slices*2+2) * m_Stacks)];
			
			colorData = new float [ (4 *(m_Slices*2+2) * m_Stacks)];
			
			int phiDx, thetaIdx;
			
			//latitude
			for (phiDx=0; phiDx < m_Stacks; phiDx++)
			{
				float phi0 = (float)Math.PI * ((float)(phiDx) * (1.0f/(float)(m_Stacks))-0.5f);
				
				float phi1 = (float)Math.PI * ((float)(phiDx+1) *(1.0f/(float)(m_Stacks))-0.5f);
				
				float cosPhi0 = (float)Math.cos(phi0);
				float sinPhi0 = (float)Math.sin(phi0);
				float cosPhi1 = (float)Math.cos(phi1);
				float sinPhi1 = (float)Math.sin(phi1);
				
				float cosTheta, sinTheta;
				
				for(thetaIdx=0; thetaIdx < m_Slices; thetaIdx++)               
            	{
	    	         //increment along the longitude circle each "slice"
	    				                                                                   
	    	         float theta = (float) (-2.0f*(float)Math.PI * ((float)thetaIdx) * (1.0/(float)(m_Slices-1)));			
	    	         cosTheta = (float)Math.cos(theta);
	    	         sinTheta = (float)Math.sin(theta);
	    	         
	    	         vertexData[vIndex] = m_Scale*cosPhi0*cosTheta;
	    	         vertexData[vIndex+1] = m_Scale*(sinPhi0*m_Squash);
	    	         vertexData[vIndex+2] = m_Scale*(cosPhi0*sinTheta);
	    	         
	    	         vertexData[vIndex+3] = m_Scale*cosPhi1*cosTheta;
	    	         vertexData[vIndex+4] = m_Scale*(sinPhi1*m_Squash); 
	    	         vertexData[vIndex+5] = m_Scale*(cosPhi1*sinTheta); 
	
	    	         colorData[cIndex+0] = (float)red;			  	
	    	         colorData[cIndex+1] = (float)0f;
	    	         colorData[cIndex+2] = (float)blue;
	          	     colorData[cIndex+4] = (float)red;
	         	     colorData[cIndex+5] = (float)0f;
	         	     colorData[cIndex+6] = (float)blue;
	         	     colorData[cIndex+3] = (float)1.0;
	    	         colorData[cIndex+7] = (float)1.0;
	
	    	         cIndex+=2*4; 	             	             	             	             	             	
	          	     vIndex+=2*3; 	
	    	         
            	}
				
				blue += colorIncrement;
				red -= colorIncrement;
				
		    	vertexData[vIndex+0] = vertexData[vIndex+3] = vertexData[vIndex-3];
		    	vertexData[vIndex+1] = vertexData[vIndex+4] = vertexData[vIndex-2]; 
		    	vertexData[vIndex+2] = vertexData[vIndex+5] = vertexData[vIndex-1];
		     }
			
		}
         
         m_VertexData = makeFloatBuffer(vertexData); 	             
         m_ColorData = makeFloatBuffer(colorData);
	}
	
	public void draw(GL10 gl)
	{
		gl.glFrontFace(GL10.GL_CW);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_VertexData);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, m_ColorData);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, (m_Slices+1)*2*(m_Stacks-1)+2);
	}
	
	protected static FloatBuffer makeFloatBuffer(float[] arr)
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}

}
