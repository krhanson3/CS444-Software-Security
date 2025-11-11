using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Fall2025_Project3_krhanson3.Data;
using Fall2025_Project3_krhanson3.Models;

namespace Fall2025_Project3_krhanson3.Controllers
{
    public class MoviesController : Controller
    {
        private readonly ApplicationDbContext _context;

        public MoviesController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: Movies
        public async Task<IActionResult> Index()
        {
            var movies = await _context.Movies.ToListAsync();
            return View(movies);
        }

        // GET: Movies/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {   return NotFound();  }

            var movies = await _context.Movies.FirstOrDefaultAsync(m => m.MovieId == id);
            if (movies == null) return NotFound();
       
            return View(movies);
        }

        // GET: Movies/Create
        public IActionResult Create()
        {
            return View();
        }

        // POST: Movies/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("MovieId,Title,Genre,ReleaseYear,IMDBUrl")] Movies movies, IFormFile? PhotoFile)
        {
            if (ModelState.IsValid)
            {
                if(PhotoFile != null && PhotoFile.Length > 0)
                {
                    using var ms = new MemoryStream();
                    await PhotoFile.CopyToAsync(ms);
                    movies.Photo = ms.ToArray();
                }

                _context.Add(movies);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(movies);
        }

        // GET: Movies/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var movies = await _context.Movies.FindAsync(id);
            if (movies == null)
            {
                return NotFound();
            }
            return View(movies);
        }

        // POST: Movies/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("MovieId,Title,Genre,ReleaseYear,IMDBUrl")] Movies updatedMovies, IFormFile? PhotoFile)
        {
            if (id != updatedMovies.MovieId) return NotFound();
           

            if (ModelState.IsValid)
            {
                try
                {
                   var existingMovie = await _context.Movies.FindAsync(id);
                     if (existingMovie == null) return NotFound();

                    // Update editable fields
                    existingMovie.Title = updatedMovies.Title;  
                    existingMovie.Genre = updatedMovies.Genre;
                    existingMovie.ReleaseYear = updatedMovies.ReleaseYear;
                    existingMovie.IMDBUrl = updatedMovies.IMDBUrl;
                    if (PhotoFile != null && PhotoFile.Length > 0)
                    {
                        using var ms = new MemoryStream();
                        await PhotoFile.CopyToAsync(ms);
                        existingMovie.Photo = ms.ToArray();
                    }
                    await _context.SaveChangesAsync();

                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!_context.Movies.Any(e => e.MovieId == updatedMovies.MovieId))
                        return NotFound();
                    else
                        throw;
                    
                }
                return RedirectToAction(nameof(Index));
            }

            var moviePoster = await _context.Movies.AsNoTracking().FirstOrDefaultAsync(m => m.MovieId == id);
            if (moviePoster != null)
            {   updatedMovies.Poster = moviePoster.Poster;  }

            return View(updatedMovies);
        }


        // GET: Movies/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var movies = await _context.Movies.FirstOrDefaultAsync(m => m.MovieId == id);
               
            if (movies == null) return NotFound();
            
            return View(movies);
        }

        // POST: Movies/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var movies = await _context.Movies.FindAsync(id);
            if (movies != null)
            {
                _context.Movies.Remove(movies);
                await _context.SaveChangesAsync();
            }
            return RedirectToAction(nameof(Index));
        }
    }
}
