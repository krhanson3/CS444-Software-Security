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
    public class ActorsController : Controller
    {
        private readonly ApplicationDbContext _context;

        public ActorsController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: Actors
        public async Task<IActionResult> Index()
        {
            var actors = await _context.Actors.ToListAsync();
            return View(actors);
        }

        // GET: Actors/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null) return NotFound();

            var actor = await _context.Actors.FirstOrDefaultAsync(a => a.ActorId == id);
            if (actor == null) return NotFound();

            return View(actor);
        }

        // GET: Actors/Create
        public IActionResult Create()
        {
            return View();
        }

        // POST: Actors/Create
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Name,Gender,Age,IMDBUrl")] Actors actor, IFormFile? PhotoFile)
        {
            if (ModelState.IsValid)
            {
                if (PhotoFile != null && PhotoFile.Length > 0)
                {
                    using var ms = new MemoryStream();
                    await PhotoFile.CopyToAsync(ms);
                    actor.Photo = ms.ToArray();
                }

                _context.Add(actor);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }

            return View(actor);
        }

        // GET: Actors/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null) return NotFound();

            var actor = await _context.Actors.FindAsync(id);
            if (actor == null) return NotFound();

            return View(actor);
        }

        // POST: Actors/Edit/5
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("ActorId,Name,Gender,Age,IMDBUrl")] Actors updatedActor, IFormFile? PhotoFile)
        {
            if (id != updatedActor.ActorId) return NotFound();

            if (ModelState.IsValid)
            {
                try
                {
                    var existingActor = await _context.Actors.FindAsync(id);
                    if (existingActor == null) return NotFound();

                    // Update editable fields
                    existingActor.Name = updatedActor.Name;
                    existingActor.Gender = updatedActor.Gender;
                    existingActor.Age = updatedActor.Age;
                    existingActor.IMDBUrl = updatedActor.IMDBUrl;

                    // Update photo only if a new one was uploaded
                    if (PhotoFile != null && PhotoFile.Length > 0)
                    {
                        using var ms = new MemoryStream();
                        await PhotoFile.CopyToAsync(ms);
                        existingActor.Photo = ms.ToArray();
                    }

                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!_context.Actors.Any(e => e.ActorId == updatedActor.ActorId))
                        return NotFound();
                    else
                        throw;
                }

                return RedirectToAction(nameof(Index));
            }

            // Re-display existing photo if validation fails
            var actorWithPhoto = await _context.Actors.AsNoTracking().FirstOrDefaultAsync(a => a.ActorId == id);
            if (actorWithPhoto != null)
                updatedActor.Photo = actorWithPhoto.Photo;

            return View(updatedActor);
        }

        // GET: Actors/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null) return NotFound();

            var actor = await _context.Actors.FirstOrDefaultAsync(a => a.ActorId == id);
            if (actor == null) return NotFound();

            return View(actor);
        }

        // POST: Actors/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var actor = await _context.Actors.FindAsync(id);
            if (actor != null)
            {
                _context.Actors.Remove(actor);
                await _context.SaveChangesAsync();
            }

            return RedirectToAction(nameof(Index));
        }
    }
}
