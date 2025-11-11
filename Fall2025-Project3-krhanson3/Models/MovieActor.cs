using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema; 

namespace Fall2025_Project3_krhanson3.Models
{
    public class MovieActor
    {
        [Key]
        public int MovieActorId { get; set; }

        [ForeignKey("Movies")]
        public int MovieId { get; set; }
        public Movies? Movie { get; set; }

        [ForeignKey("Actors")]
        public int ActorId { get; set; }
        public Actors? Actor { get; set; }
    }
}
