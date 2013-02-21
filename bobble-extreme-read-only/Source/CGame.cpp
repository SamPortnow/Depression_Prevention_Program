
#include "CGame.h"
#include "OpenGL\\gltools.h"
#include "OpenGL\\math3d.h"

#define KEYDOWN(key) ((GetAsyncKeyState(key) & 0x80000000) ? true : false)

////////////////////////////////////////////////////////////////////////////
// Do not put any OpenGL code here. General guidance on constructors in 
// general is to not put anything that can fail here either (opening files,
// allocating memory, etc.)
CGame::CGame(void) : _screenWidth(800), _screenHeight(600), _bFullScreen(false), _bAnimated(true)
{
	
	
	
}
	
	
///////////////////////////////////////////////////////////////////////////////
// OpenGL related startup code is safe to put here. Load textures, etc.
void CGame::Initialize(void)
{
	// Ugly plumb red picked on purpose... no a likely color to occur by accident.
	// So if you see this color, it means it's working ;-)
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

	//	Enable Depth Buffer
	glEnable(GL_DEPTH_TEST);
	
	//	Wireframe mode
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

	//	Set the camera back some
	_Frame.MoveForward(-2.5f);
}


///////////////////////////////////////////////////////////////////////////////
// Do your cleanup here. Free textures, display lists, buffer objects, etc.
void CGame::Shutdown(void)
{
	
}


///////////////////////////////////////////////////////////////////////////////
// This is called at least once and before any rendering occurs. If the screen
// is a resizeable window, then this will also get called whenever the window
// is resized.
void CGame::Resize(GLsizei nWidth, GLsizei nHeight)
{
	glViewport(0, 0, nWidth, nHeight);

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();

	gluPerspective(35, (GLdouble)nWidth / (GLdouble)nHeight, 1, 1000);

	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();	
}


///////////////////////////////////////////////////////////////////////////////
// Render a frame. The owning framework is responsible for buffer swaps,
// flushes, etc.
void CGame::Render(void)
{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	float dt = _Timer.GetElapsedSeconds();

	//	Create Quadric object
	GLUquadricObj *pObj;
	pObj = gluNewQuadric();

	glPushMatrix();
	_Frame.ApplyCameraTransform();

	//	Render Sun
	RenderSphereObject(0.6f, 26, 13, 0.0f, 0.0f, 0.0f, 45.0f, dt, 0.0f, 1.0f, 0.5f, 0.0f);
	
	//	Render Earth and Moon
	RenderSphereObject(0.2f, 26, 13, 0.0f, 0.0f, -1.5f, 33.0f, dt, 45.0f, 0.0f, 1.0f, 0.5f);
	RenderSphereObject(0.05f, 26, 13, 0.0f, 0.0f, -1.8f, 33.0f, dt, 45.0f, 0.5f, 0.5f, 0.5f, true, 110.0f, -0.05f);

	//	Render Mars and Two Moons
	RenderSphereObject(0.25f, 26, 13, 0.0f, 0.0f, -3.0f, 46.0f, dt, 38.0f, 1.0f, 0.0f, 0.0f);
	RenderSphereObject(0.05f, 26, 13, 0.0f, 0.0f, -3.0f, 33.0f, dt, 38.0f, 0.5f, 0.5f, 0.5f, true, -170.0f, -0.35f);
	RenderSphereObject(0.07f, 26, 13, 0.0f, 0.0f, -3.0f, 33.0f, dt, 38.0f, 0.5f, 0.5f, 0.5f, true, -170.0f, 0.35f);

	//	Render Saturn
	RenderSphereObject(0.5f, 26, 13, 0.0f, 0.0f, -4.5f, 25.0f, dt, 32.0f, 0.0f, 0.2f, 1.0f);
	
	for(GLfloat i = 0.0f; i < 0.6f; i+=0.15f)
	{
		glPushMatrix();
		glRotatef(32.0f * dt, 0.0f, 1.0f, 0.0f);
		glTranslatef(0.0f, 0.0f, -4.5f);
		glRotatef(75.0f, 1.0f, 0.0f, 0.0f);
		glRotatef(25.0f * dt, 0.0f, 0.0f, 1.0f);
		glColor3f(0.7f, 0.7f, 0.7f);
		gluDisk(pObj, 0.55+i, 0.6+i, 26, 13);
		glPopMatrix();
	}

	//	Render Comet
	RenderSphereObject(0.08f, 26, 13, 0.0f, 0.0f, -6.0f, 0.0f, dt, 60.0f, 0.4f, 0.4f, 0.4f);
	{ glPushMatrix();
		glRotatef(60.0f * dt, 0.0f, 1.0f, 0.0f);
		glTranslatef(0.0f, 0.0f, -6.0f);
		glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
		glColor3f(1.0f, 1.0f, 0.0f);
		gluCylinder(pObj, 0.05, 0.0f, 0.5f, 26, 13);
	} glPopMatrix();

	gluDeleteQuadric(pObj);
	glPopMatrix();
}

// Updates the objects within the class
void CGame::Update(void)
{
	static CStopWatch timer;
	float fTime = timer.GetElapsedSeconds();
	timer.Reset();

	float fLinear = 3.0f * fTime;
	float fAngular = 60.0f * fTime;

	if(KEYDOWN(VK_UP))
		_Frame.MoveForward(fLinear);

	if(KEYDOWN(VK_DOWN))
		_Frame.MoveForward(-fLinear);

	if(KEYDOWN(VK_LEFT))
		_Frame.RotateLocalY((float)m3dDegToRad(fAngular));

	if(KEYDOWN(VK_RIGHT))
		_Frame.RotateLocalY((float)m3dDegToRad(-fAngular));
}

// Render's a sphere object to the screen, rotated and ready to go.
void CGame::RenderSphereObject(GLfloat radius, GLfloat slices, GLfloat cuts, GLfloat x, GLfloat y, GLfloat z, GLfloat speed, GLfloat dt, GLfloat orbit, GLfloat colR, GLfloat colG, GLfloat colB, bool bIsMoon, GLfloat moonRot, GLfloat xOff, GLfloat yOff, GLfloat zOff)
{
	glPushMatrix();
	float yRot = dt * orbit;
	float xRot = dt * speed;
	moonRot *= dt;

	glRotatef(yRot, 0.0f, 1.0f, 0.0f);
	glTranslatef(x, y, z);
	
	glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
	glRotatef(xRot, 0.0f, 0.0f, 1.0f);
	
	if(bIsMoon)
	{
		glRotatef(moonRot, 0.0f, 0.0f, 1.0f);
		glTranslatef(xOff, yOff, zOff);
	}

	glColor3f(colR, colG, colB);
	gltDrawSphere(radius, (int)slices, (int)cuts);

	glPopMatrix();
}
