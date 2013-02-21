
#ifndef __CGame__
#define __CGame__

// Windows headers
#ifdef WIN32
#include <windows.h>
#include "OpenGL\\GLee.h"
#include <gl/gl.h>
#include <gl/glu.h>
#include "OpenGL\\glframe.h"
#include "OpenGL\\stopwatch.h"

#pragma comment(lib, "opengl32.lib")
#pragma comment(lib, "glu32.lib")
#endif


class CGame
	{
	public:
		CGame();
		virtual ~CGame() {};

		void Initialize(void);							// Called after context is created
		void Shutdown(void);							// Called before context is destroyed
		void Resize(GLsizei nWidth, GLsizei nHeight);	// Called when window is resized, at least once when window is created
		void Render(void);								// Called to update OpenGL view
		void Update(void);								// Takes care of time based stuff
		
		//	Render's a sphere
		void RenderSphereObject(GLfloat radius, GLfloat slices, GLfloat cuts, GLfloat x, GLfloat y, GLfloat z, GLfloat speed, GLfloat dt, GLfloat orbit, GLfloat colR, GLfloat colG, GLfloat colB, bool bIsMoon = false, GLfloat moonRot = 0.0f, GLfloat xOff = 0.0f, GLfloat yOff = 0.0f, GLfloat zOff = 0.0f);
		
		// These methods are used by the calling framework. Set the appropriate internal
		// protected variables to tell the parent framework your desired configuration
		inline GLuint GetWidth(void) { return _screenWidth; }
		inline GLuint GetHeight(void) { return _screenHeight; }
		inline GLboolean GetFullScreen(void) { return _bFullScreen; }
		inline GLboolean GetAnimated(void) { return _bAnimated; }
			
			
	protected:
		GLsizei	 _screenWidth;			// Desired window or desktop width
		GLsizei  _screenHeight;			// Desired window or desktop height
		
		GLboolean _bFullScreen;			// Request to run full screen
		GLboolean _bAnimated;			// Request for continual updates
		GLFrame	  _Frame;				// Camera object
		CStopWatch _Timer;				// Timer object

	};
		
#endif // __CGame__