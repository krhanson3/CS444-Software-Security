using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Fall2025_Project3_krhanson3.Models;

namespace Fall2025_Project3_krhanson3.Data
{
    public class ApplicationDbContext : IdentityDbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
            : base(options)
        {
        }
        public DbSet<Fall2025_Project3_krhanson3.Models.Actors> Actors { get; set; } = default!;
        public DbSet<Fall2025_Project3_krhanson3.Models.Movies> Movies { get; set; } = default!;
        public DbSet<Fall2025_Project3_krhanson3.Models.MovieActor> MovieActor { get; set; } = default!;
    }
}
