using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.AspNetCore.Http;

namespace Fall2025_Project3_krhanson3.Models
{
    public class Actors
    {
        [Key]
        public int ActorId { get; set; }

        [Required]
        public string Name { get; set; } = string.Empty;

        public string? Gender { get; set; }

        public int Age { get; set; }

        [Url]
        public string? IMDBUrl { get; set; }

        // Stored in DB
        public byte[]? Photo { get; set; }

        // Used only for uploads
        [NotMapped]
        public IFormFile? PhotoFile { get; set; }
    }
}
